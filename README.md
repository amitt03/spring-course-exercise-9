
Exercise 9
==========

Have a look inside dao/impl package.<br/>
Notice that there is a new dao implementation named **'BookUsuallyInMemoryDao2'**.<br/>
This dao is an extension of the regular **BookInMemoryDao** except that it can be configured to be a *moody* dao and it sometimes throws an exception:<br/>
* MoodyException("I am really not in the mood to do that...")

1. Try running the regular test **LibraryControllerTest** (currently ignore its new sibling **LibraryControllerTest2**).<br/>
   As you see the tests fail... why?<br/>
   Fix the test using profiles and make sure that **LibraryControllerTest** is running only with **BookInMemoryDao**

2. Adjust **LibraryControllerTest2** to work only with **BookUsuallyInMemoryDao2**.<br/>
   Make sure all test pass successfully.

3. Update **web.xml** so only **BookInMemoryDao** will be loaded when the tomcat is run.<br/>
   Start the tomcat and see that it loads successfully

4. Expose **BookInMemoryDao** to JMX (don't forget the jmx configuration)<br/>
   Expose *dataStoreName* attribute (both getter and setter)<br/>
   Expose *getBooksInventoryCount* operation<br/>
   Expose *getLoanedBooksInventoryCount* operation<br/>
   Expose *removeBook* **and** *returnBook* operation

5. Run the tomcat, open JConsole and see your exposed MBean<br/>
   Use swagger and the JConsole to verify that all of your jmx operations work as expected

###N O T I C E
- [ ] JMX port is configured inside the tomcat setting, usually the default is 1099<br/>
   In order to run JConsole, go to the windows start menu -> run -> jconsole host:port (i.e. jconsole localhost:1099)


