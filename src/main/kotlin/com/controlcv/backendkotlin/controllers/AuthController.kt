package com.controlcv.backendkotlin.controllers

import com.controlcv.backendkotlin.dtos.LoginDTO
import com.controlcv.backendkotlin.dtos.Message
import com.controlcv.backendkotlin.dtos.RegisterDTO
import com.controlcv.backendkotlin.models.User
import com.controlcv.backendkotlin.services.UserService
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CookieValue
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.lang.Exception
import java.util.Date

@RestController
@RequestMapping("api")
class AuthController(private val userService:UserService) {

    @PostMapping("register")
    fun register(@RequestBody body: RegisterDTO): ResponseEntity<User>{
        val user = User()
        user.name = body.name
        user.email = body.email
        user.password = body.password
        return ResponseEntity.ok(this.userService.save(user))
    }

    @PostMapping("login")
    fun login(@RequestBody body: LoginDTO, response: HttpServletResponse): ResponseEntity<Any> {
        val user = userService.findByEmail(body.email) ?: return ResponseEntity.badRequest().body(Message("User not found."))

        if(!user.comparePassword(body.password)){
            return ResponseEntity.badRequest().body(Message("Invalid password."))
        }

        val jwt = Jwts.builder()
            .setIssuer(user.id.toString())
            .setExpiration(Date(System.currentTimeMillis() + 60 * 24 * 1000))
            .signWith(SignatureAlgorithm.HS512, "secret").compact()

        val cookie = Cookie("jwt", jwt)
        cookie.isHttpOnly = true

        response.addCookie(cookie)

        return ResponseEntity.ok(Message("success"))
    }

    @GetMapping("user")
    fun user(@CookieValue("jwt") jwt: String?): ResponseEntity<Any> {
        try{
            if(jwt == null){
                return ResponseEntity.status(401).body(Message("unauthenticated."))
            }
            val body = Jwts.parser().setSigningKey("secret").parseClaimsJws(jwt).body
            return ResponseEntity.ok(this.userService.getById(body.issuer.toInt()))
        } catch (e: Exception){
            return ResponseEntity.status(401).body(Message("unauthenticated."))
        }
    }

    @PostMapping("logout")
    fun logout(respone: HttpServletResponse): ResponseEntity<Any>{
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0

        respone.addCookie(cookie)
        return ResponseEntity.ok(Message("success"))
    }
}