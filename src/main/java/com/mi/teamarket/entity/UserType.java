package com.mi.teamarket.entity;


import java.util.Map;
import java.util.Set;

public class UserType {
    public static String CUSTOMER = "customer";
    public static String EXPERT = "expert";
    public static String STAFF = "staff";
    public static String SERVER = "server";
    public static String OWNER = "owner";

    private static final Set<String> VALID_TYPES = Set.of(CUSTOMER, EXPERT, STAFF, SERVER, OWNER);
    private static final Map<String, String> TYPE_MAPPING = Map.of(
            CUSTOMER, "顾客",
            EXPERT, "茶文化专家",
            STAFF, "普通员工",
            SERVER, "客服员工",
            OWNER, "店主"
    );

    public static boolean check(String... userTypes) {
        if (userTypes == null) return false;
        for (String type : userTypes) {
            if (!VALID_TYPES.contains(type)) {
                return false;
            }
        }
        return true;
    }

    public static String mapping(String userType) {
        if (userType == null) return "未知类型";
        return TYPE_MAPPING.getOrDefault(userType.toLowerCase(), "未知类型");
    }

}
