package com.common.configClasses;

public class CaboryaService {

    private String annotationScanPackage;

    private String expiredTokenDeleteCronExpression;

    public String getAnnotationScanPackage() {
        return annotationScanPackage;
    }

    public void setAnnotationScanPackage(String annotationScanPackage) {
        this.annotationScanPackage = annotationScanPackage;
    }

    public String getExpiredTokenDeleteCronExpression() {
        return expiredTokenDeleteCronExpression;
    }

    public void setExpiredTokenDeleteCronExpression(String expiredTokenDeleteCronExpression) {
        this.expiredTokenDeleteCronExpression = expiredTokenDeleteCronExpression;
    }
}
