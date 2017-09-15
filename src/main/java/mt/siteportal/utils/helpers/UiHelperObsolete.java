package mt.siteportal.utils.helpers;

public class UiHelperObsolete {
/*	
	 private static final Logger logger = LoggerFactory.getLogger(UiHelper.class);

	    //TODO: Make configurable.
	    protected static final int WAITFOR_TIMEOUT = 32; // seconds

	    private static final int DEFAULT_SLEEP_TIMEOUT = 1000; // milliseconds
	   
        
        *//**
         * Tries to raise submit on element specified by the locator.
         *
         * @param by- the target element's locator.
         *//*
        public static void submit(By by) {
            submit(findElement(by));
        }
        
        *//**
         * Checks whether element specified by id is present on current page.
         *
         * @param id - element's id.
         *
         * @return True if the element present on the page.
         *//*
        public static boolean isPresentById(String id) {
            logger.debug("isPresentById(): " + id);
            return isPresent(By.id(id));
        }

        *//**
         * Simple wrapper around driver.findElement().
         * Simplifies logging and extending.
         *
         * @param by - element-to-find locator.
         *
         * @return WebElement found.
         *//*
        public static WebElement findElement(By by) {
            logger.debug("findElement() By: " + by.toString());

            return Browser.getDriver().findElement(by);
        }
        
    
        
        *//**
         * Checks whether element specified by the locator is present on current page.
         *
         * @param by - locator of the element.
         *
         * @return True if the element present on the page.
         *//*
        public static boolean isPresent(By by) {
            logger.debug("isPresent() By: " + by.toString());

            return Browser.getDriver().findElements(by).size() > 0;	
        }
        
        *//**
         * Tries to raise submit on the element.
         * @param element - the target element.
         *//*
        public static void submit(WebElement element) {
            logger.debug("Submitting " + element);
            element.submit();
        }
        

        *//**
         * Releases left mouse button.
         *//*
        public static void release() {
            logger.debug("Releasing left mouse button.");
            getWebDriverActions().release().perform();
        }



    }*/
}
