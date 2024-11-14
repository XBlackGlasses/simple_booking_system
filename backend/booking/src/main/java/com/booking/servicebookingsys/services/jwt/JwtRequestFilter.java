package com.booking.servicebookingsys.services.jwt;

import com.booking.servicebookingsys.utill.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // get authorization(授權) from request
        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;
        // 在 OAuth 2.0 的授權規範中，JWT 在 Request 的傳送格式是：
        // 放在 Header 的 “Authorization” 欄位裡，並且以 Bearer 開頭。
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);    // 略過bearer開頭
            username = jwtUtil.extractUsername(token);  // email
            System.out.printf(username + " pass the authorization \n");
        }

        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtUtil.validateToken(token, userDetails)) {
                // 產生authToken
                UsernamePasswordAuthenticationToken authToken = new
                    UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 更新用戶訊息，這次連線中保存登入狀態
                // spring boot 在這保存 誰已經驗證 的詳細資訊
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
