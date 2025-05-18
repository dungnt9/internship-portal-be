package com.dungnguyen.cloudgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

@Configuration
public class RouteRoleConfig {

    @Bean
    public Map<String, Set<String>> routeRoles() {
        Map<String, Set<String>> routeRoles = new HashMap<>();

        routeRoles.put("/auth/login", Collections.emptySet());
        routeRoles.put("/auth/register", Collections.emptySet());
        routeRoles.put("/auth/forgot-password", Collections.emptySet());
        routeRoles.put("/auth/verify-otp", Collections.emptySet());
        routeRoles.put("/auth/reset-password", Collections.emptySet());
        routeRoles.put("/auth/change-password", Set.of("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_COMPANY", "ROLE_STUDENT"));

        routeRoles.put("/user/admin/**", Set.of("ROLE_ADMIN"));
        routeRoles.put("/user/teachers/**", Set.of("ROLE_ADMIN", "ROLE_TEACHER"));
        routeRoles.put("/user/companies/me", Set.of("ROLE_ADMIN", "ROLE_COMPANY"));
        routeRoles.put("/user/companies/my", Set.of("ROLE_ADMIN", "ROLE_COMPANY"));
        routeRoles.put("GET:/user/companies/all", Set.of("ROLE_ADMIN", "ROLE_COMPANY", "ROLE_STUDENT", "ROLE_TEACHER"));
        routeRoles.put("GET:/user/companies/**", Set.of("ROLE_ADMIN", "ROLE_COMPANY", "ROLE_STUDENT", "ROLE_TEACHER"));
        routeRoles.put("PUT:/user/companies/[0-9]+", Set.of("ROLE_ADMIN"));
        routeRoles.put("/user/students/**", Set.of("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"));
        routeRoles.put("GET:/user/students/[0-9]+", Set.of("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_COMPANY"));

        routeRoles.put("/registration/positions/all", Set.of("ROLE_ADMIN"));
        routeRoles.put("/registration/periods", Set.of("ROLE_ADMIN", "ROLE_COMPANY", "ROLE_TEACHER"));
        routeRoles.put("/registration/positions/company/**", Set.of("ROLE_ADMIN", "ROLE_STUDENT", "ROLE_COMPANY", "ROLE_TEACHER"));
        routeRoles.put("/registration/periods/current", Set.of("ROLE_ADMIN", "ROLE_STUDENT", "ROLE_COMPANY", "ROLE_TEACHER"));
        routeRoles.put("/registration/periods/upcoming", Set.of("ROLE_ADMIN", "ROLE_STUDENT", "ROLE_COMPANY", "ROLE_TEACHER"));

        routeRoles.put("GET:/registration/external-internships/me", Set.of("ROLE_STUDENT"));
        routeRoles.put("POST:/registration/external-internships", Set.of("ROLE_STUDENT"));
        routeRoles.put("PUT:/registration/external-internships/[0-9]+/cancel", Set.of("ROLE_STUDENT"));

        routeRoles.put("GET:/registration/applications/me", Set.of("ROLE_STUDENT"));
        routeRoles.put("POST:/registration/applications/upload-cv", Set.of("ROLE_STUDENT"));
        routeRoles.put("POST:/registration/applications/register-preferences", Set.of("ROLE_STUDENT"));
        routeRoles.put("PUT:/registration/applications/[0-9]+/update-preferences", Set.of("ROLE_STUDENT"));

        routeRoles.put("/registration/company-positions", Set.of("ROLE_COMPANY"));
        routeRoles.put("PUT:/registration/company-positions/[0-9]+", Set.of("ROLE_COMPANY"));

        routeRoles.put("/registration/company-applications/pending", Set.of("ROLE_COMPANY"));
        routeRoles.put("/registration/company-applications/action", Set.of("ROLE_COMPANY"));
        routeRoles.put("/registration/company-applications/history", Set.of("ROLE_COMPANY"));

        routeRoles.put("/registration/company-progress", Set.of("ROLE_COMPANY"));

        routeRoles.put("/evaluation/**", Set.of("ROLE_ADMIN", "ROLE_TEACHER"));

        return routeRoles;
    }

    public static boolean isRouteAccessibleByRole(String url, String role, String method, Map<String, Set<String>> routeRoles) {
        if (role == null || role.isEmpty()) {
            return isPublicRoute(url, routeRoles);
        }

        // Check method-specific patterns first
        String methodPattern = method + ":" + url;
        for (Map.Entry<String, Set<String>> entry : routeRoles.entrySet()) {
            String pattern = entry.getKey();
            Set<String> allowedRoles = entry.getValue();

            if (pattern.startsWith(method + ":")) {
                // For method-specific patterns
                String patternPath = pattern.substring(method.length() + 1);
                if (matchesPattern(url, patternPath)) {
                    if (allowedRoles.isEmpty()) {
                        return true;
                    }
                    return allowedRoles.contains(role);
                }
            }
        }

        // Fall back to generic patterns (which cover both GET and PUT for /my and /me)
        for (Map.Entry<String, Set<String>> entry : routeRoles.entrySet()) {
            String pattern = entry.getKey();
            Set<String> allowedRoles = entry.getValue();

            if (!pattern.contains(":") && matchesPattern(url, pattern)) {
                if (allowedRoles.isEmpty()) {
                    return true;
                }
                return allowedRoles.contains(role);
            }
        }

        return false;
    }

    public static boolean isPublicRoute(String url, Map<String, Set<String>> routeRoles) {
        for (Map.Entry<String, Set<String>> entry : routeRoles.entrySet()) {
            String pattern = entry.getKey();
            Set<String> allowedRoles = entry.getValue();

            if (!pattern.contains(":") && matchesPattern(url, pattern) && allowedRoles.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    private static boolean matchesPattern(String url, String pattern) {
        if (pattern.equals(url)) {
            return true;
        }

        if (pattern.endsWith("/**")) {
            String basePattern = pattern.substring(0, pattern.length() - 2);
            return url.startsWith(basePattern);
        }

        // Handle regex patterns (for numerical IDs)
        if (pattern.contains("[0-9]+")) {
            String regexPattern = "^" + pattern.replace("[0-9]+", "\\d+") + "$";
            return url.matches(regexPattern);
        }

        return false;
    }
}