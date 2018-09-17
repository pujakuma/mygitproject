/*
 * Copyright (c) All Right Reserved, http://www.corenttech.com
 *
 * This file is subject to the terms and conditions defined in file 'LICENSE.txt', which is part of this source code package.
 */
package com.corenttech.engine.saasification.config;

/**
 *
 * @author azera
 */
public final class Constants {

    private Constants() {
        super();
    }
    public static final String ERROR_START = "<error version=\"1.0\">";
    public static final String ERROR_END = "</error>";
    public static final String XML_HEADER = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
    public static final String ACTIVE = "Active";
    public static final String TEST_INACTIVE = "TestInactive";
    public static final String DEACTIVE = "Deactive";
    public static final String TRASH = "Trash";
    public static final String FORCETRASH = "ForceTrash";
    public static final String INPROGRESS = "In progress";
    public static final String UPGRADE = "Upgrade";
    public static final String UTFFORMAT = "UTF-8";
    /*
     * 
     */
    public static final String ID = "id";
    public static final String COMPLETE = "completed";
    /*
     * Response Xmls
     */
    public static final String ERROR_START_500 = "<response code=\"500\">";
    public static final String SUCCESS_START_200 = "<response code=\"200\">";
    public static final String SUCCESS_HTTP_START_200 = "<response httpstatuscode=\"200\">";
    public static final String SUCCESS_START_201 = "<response code=\"201\">";
    public static final String END_RESPONSE = "</response>";
    public static final String GET_SUCCESS = "<response  success=\"true\" code=\"200\">";
    /*
     * DataCheck
     */
    public static final String PARENT = "parent";
    public static final String ACCOUNT = "account";
    /*
     * Action
     */
    public static final String CREATE = "create";
    public static final String UPDATE = "update";
    public static final String GET = "get";
    public static final String LIST = "list";
    public static final String DELETE = "delete";
    public static final String CREATE_UPDATE = "CreateOrUpdate";
    public static final String UPDATE_CREDITCARD = "UpdateCreditCard";
    public static final String POST_USAGE = "postUsage";
    public static final String UPDATE_EXPIRYDATE = "UpdateExpiryDate";
    /*
     * Spec keys
     */
    public static final String SPEC = "spec";
    /*
     * Service Response
     */
    public static final String ERROR_500 = "error_500";
    public static final int SUCCESS_RESPONSE = 200;
    /*
     * other
     */
    public static final String MODULE = "SaaSification";
}
