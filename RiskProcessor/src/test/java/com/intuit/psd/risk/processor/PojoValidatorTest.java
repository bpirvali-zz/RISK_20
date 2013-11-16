package com.intuit.psd.risk.processor;

import java.util.List;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.openpojo.reflection.PojoClass;
import com.openpojo.reflection.filters.FilterPackageInfo;
import com.openpojo.reflection.impl.PojoClassFactory;
import com.openpojo.validation.PojoValidator;
import com.openpojo.validation.rule.impl.NoPublicFieldsRule;
import com.openpojo.validation.test.impl.GetterTester;
import com.openpojo.validation.test.impl.SetterTester;

public class PojoValidatorTest {
	// The package to test
    private static final String POJO_PACKAGE = "com.intuit.psd.risk.processor.card";

    private List<PojoClass> pojoClasses;
    private PojoValidator pojoValidator;

	/*
	@BeforeTest
    public void setup() {
        pojoClasses = PojoClassFactory.getPojoClasses(POJO_PACKAGE, new FilterPackageInfo());

        pojoValidator = new PojoValidator();

        // Create Rules to validate structure for POJO_PACKAGE
        pojoValidator.addRule(new NoPublicFieldsRule());
//        pojoValidator.addRule(new NoPrimitivesRule());
//        pojoValidator.addRule(new NoStaticExceptFinalRule());
//        pojoValidator.addRule(new GetterMustExistRule());
//        pojoValidator.addRule(new SetterMustExistRule());
//        pojoValidator.addRule(new NoNestedClassRule());
//        pojoValidator.addRule(new BusinessKeyMustExistRule());

        // Create Testers to validate behaviour for POJO_PACKAGE
        //pojoValidator.addTester(new DefaultValuesNullTester());
        pojoValidator.addTester(new SetterTester());
        pojoValidator.addTester(new GetterTester());
        //pojoValidator.addTester(new BusinessIdentityTester());
    }
*/

    /*@Test
    public void testPojoStructureAndBehavior() {
        for (PojoClass pojoClass : pojoClasses) {
            pojoValidator.runValidation(pojoClass);
        }
    }*/
}
