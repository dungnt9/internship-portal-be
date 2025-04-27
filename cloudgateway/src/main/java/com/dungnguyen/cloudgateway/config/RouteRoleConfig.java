package com.dungnguyen.cloudgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;

/**
 * Configuration class that defines which roles can access specific URL patterns
 */
@Configuration
public class RouteRoleConfig {

    /**
     * Returns a map of URL patterns to allowed roles
     * Key: URL pattern (Ant-style pattern)
     * Value: Set of roles that can access the URL
     *
     * @return Map of URL patterns to allowed roles
     */
    @Bean
    public Map<String, Set<String>> routeRoles() {
        Map<String, Set<String>> routeRoles = new HashMap<>();

        // Define public routes (no authentication required)
        routeRoles.put("/auth/login", Collections.emptySet());
        routeRoles.put("/auth/register", Collections.emptySet());

        // Admin routes
        routeRoles.put("/user/admin/**", Set.of("ROLE_ADMIN"));

        // Routes for teachers
        routeRoles.put("/user/teachers/**", Set.of("ROLE_ADMIN", "ROLE_TEACHER"));
        routeRoles.put("/evaluation/**", Set.of("ROLE_ADMIN", "ROLE_TEACHER"));

        // Routes for companies
        routeRoles.put("/user/companies/**", Set.of("ROLE_ADMIN", "ROLE_COMPANY"));

        // Routes for students
        routeRoles.put("/user/students/**", Set.of("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_STUDENT"));
        routeRoles.put("/registration/**", Set.of("ROLE_ADMIN", "ROLE_STUDENT", "ROLE_COMPANY", "ROLE_TEACHER"));

        // Default route permissions (accessible by all authenticated users)
        routeRoles.put("/**", Set.of("ROLE_ADMIN", "ROLE_TEACHER", "ROLE_COMPANY", "ROLE_STUDENT"));

        return routeRoles;
    }

    /**
     * Utility method to check if a given URL is accessible by a specific role
     *
     * @param url The URL to check
     * @param role The role to check
     * @param routeRoles Map of URL patterns to allowed roles
     * @return true if the role can access the URL, false otherwise
     */
    public static boolean isRouteAccessibleByRole(String url, String role, Map<String, Set<String>> routeRoles) {
        // If no role is provided, check if the URL is public
        if (role == null || role.isEmpty()) {
            return isPublicRoute(url, routeRoles);
        }

        // Check each route pattern
        for (Map.Entry<String, Set<String>> entry : routeRoles.entrySet()) {
            String pattern = entry.getKey();
            Set<String> allowedRoles = entry.getValue();

            if (matchesPattern(url, pattern)) {
                // If allowedRoles is empty, it's a public route
                if (allowedRoles.isEmpty()) {
                    return true;
                }

                // Check if the role is allowed
                return allowedRoles.contains(role);
            }
        }

        // By default, deny access
        return false;
    }

    /**
     * Check if a URL is public (no authentication required)
     *
     * @param url The URL to check
     * @param routeRoles Map of URL patterns to allowed roles
     * @return true if the URL is public, false otherwise
     */
    public static boolean isPublicRoute(String url, Map<String, Set<String>> routeRoles) {
        for (Map.Entry<String, Set<String>> entry : routeRoles.entrySet()) {
            String pattern = entry.getKey();
            Set<String> allowedRoles = entry.getValue();

            if (matchesPattern(url, pattern) && allowedRoles.isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Simple pattern matching for URL patterns
     * Supports * wildcard for path segments
     *
     * @param url The URL to check
     * @param pattern The pattern to match against
     * @return true if the URL matches the pattern, false otherwise
     */
    private static boolean matchesPattern(String url, String pattern) {
        // Exact match
        if (pattern.equals(url)) {
            return true;
        }

        // Path pattern with wildcard
        if (pattern.endsWith("/**")) {
            String basePattern = pattern.substring(0, pattern.length() - 2);
            return url.startsWith(basePattern);
        }

        return false;
    }
}