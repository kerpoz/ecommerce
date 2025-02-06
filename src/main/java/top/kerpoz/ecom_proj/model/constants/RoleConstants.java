package top.kerpoz.ecom_proj.model.constants;

import org.springframework.stereotype.Component;
import top.kerpoz.ecom_proj.model.enums.RoleType;

@Component("roleConstants")
public class RoleConstants {
    public static final String ROLE_USER = RoleType.ROLE_USER.name();
    public static final String ROLE_ADMIN = RoleType.ROLE_ADMIN.name();
}