package library;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class RoleTest {

 
    @Test
    void testRoleValuesCount() {
        Role[] roles = Role.values();
        assertEquals(2, roles.length, "Role enum must contain exactly 2 values");
    }

  
    @Test
    void testRoleContainsAdmin() {
        assertNotNull(Role.valueOf("ADMIN"), "Role enum should contain ADMIN");
    }

   
    @Test
    void testRoleContainsSuperAdmin() {
        assertNotNull(Role.valueOf("SUPER_ADMIN"), "Role enum should contain SUPER_ADMIN");
    }

    
    @Test
    void testRoleToString() {
        assertEquals("ADMIN", Role.ADMIN.toString());
        assertEquals("SUPER_ADMIN", Role.SUPER_ADMIN.toString());
    }
}
