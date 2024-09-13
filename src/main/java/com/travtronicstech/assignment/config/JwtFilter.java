package com.travtronicstech.assignment.config;

import com.travtronicstech.assignment.service.JWTService;
import com.travtronicstech.assignment.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JWTService jwtService;

    @Autowired
    ApplicationContext context;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        String authHeader=request.getHeader("Authorization");
        String token=null;
        String username=null;

        if(authHeader!=null && authHeader.startsWith("Bearer ")){

            token=authHeader.substring(7);
            try {
                username = jwtService.extractUserName(token);

            } catch (Exception e) {
                System.out.println("JWT Token parsing failed: " + e.getMessage());
            }
        }


        if (username!=null && SecurityContextHolder.getContext().getAuthentication()==null){

            UserDetails userDetails= context.getBean(MyUserDetailsService.class).loadUserByUsername(username);

//            System.out.println("True or False:"+jwtService.validateToken(token, userDetails));

            if(jwtService.validateToken(token, userDetails)){

//                System.out.println("authorities:"+userDetails.getAuthorities());

                UsernamePasswordAuthenticationToken authToken= new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());

//                System.out.println("authtoken:"+authToken);

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request,response);
    }
}
