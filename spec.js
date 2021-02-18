const { browser } = require("protractor");

describe('Protractor steps',function() {  

    it('Open Angular js website',function() {
    
    //write your protractor raw code
        
        browser.get('http://www.angularjs.org');
        // browser.wait(EC.presenceOf(element(by.model('yourName'))),3500);
        element(by.model('yourName')).sendKeys('Tom');
    
    });
    //Each it block will be called as a spec
    
});