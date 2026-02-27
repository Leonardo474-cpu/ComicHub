package com.comic.hub.config;

import com.comic.hub.model.Usuario;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthSessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Prevent browser caching on private admin pages.
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        HttpSession session = request.getSession(false);

        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        Object usuarioEnSesion = session.getAttribute("usuarioLogueado");
        if (!(usuarioEnSesion instanceof Usuario usuario)) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        if (usuario.getRol() == null || !"ADMIN".equals(usuario.getRol().getNombreRol())) {
            response.sendRedirect(request.getContextPath() + "/login");
            return false;
        }

        return true;
    }
}
