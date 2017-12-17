# Plugin Development Support

If you want to help in the development of this plugin, you can send us PR (Pull Request). All the help is welcome; and we have [**a list**](https://github.com/rentalhost/laravel-insight/issues) with some features that we would like to see implemented.

## Things you need to know

1. Before you start implementing a change, [**open an issue ticket**](https://github.com/rentalhost/laravel-insight/issues/new) so we can discuss its feasibility. It may be the wrong plugin for which you want to deploy that code.
1. Try to work on changes that were requested in [**the next milestone**](https://github.com/rentalhost/laravel-insight/milestones). But if you're excited about a future request, enjoy the moment!
1. Do not be upset if we have to make minor changes to your code or rewrite it. Understand that we can limit ourselves to using your help fully, partially or intellectually (the logic that you have used, for example). And this can happen for several reasons:
    1. The code is not following our code style;
    1. There are some possible improvements in the code structure;
    1. And other reasons we will think about when it occurs;
1. The code needs to be developed as non-profit and be in the public domain - and preferably have been developed entirely by you.
    1. We currently have no financial plans for this project. However, this can happen without prior notice. So be aware that you will not be compensated for the time you have been supporting the project - but do not worry: the public code you send will remain public.
1. All codes submitted need to be programmatically tested and, if possible, cover all developed code (code coverage). Tests need to be set up for expected and unexpected returns.
1. Make small commits with small responsibilities - but that are enough not to get so insignificant. For example: if to a change is needed to make some minor changes to the code before actually starting to work on what you want to change, separate it into two commits (the first for code setup, and the next for the real change).
    1. The commit message should be clear, short, and easy to understand without having to read the code. If the change was in a specific class, for example, put it as subject and then describe what was changed. You can see [**our latest commits**](https://github.com/rentalhost/laravel-insight/commits/master) to understand how to format it.

## Preparing the Environment

1. Install last [**IntelliJ IDEA Ultimate**](https://www.jetbrains.com/idea/) - unfortunatelly I think that is impossible install on Community version because of some plugins dependencies;
1. Install the plugins **PHP** and **Blade**;
1. Fork this repository and clone to your local environment;
1. Import the project [**Code Style**](Laravel%20Insight%20-%20Code%20Style.xml) and base [**Inspections**](Laravel%20Insight%20-%20Inspections.xml) settings;
1. Open the project folder;
1. In the **Project Structure** you will probably need to make some minor modifications. Setup it as the follow:
    1. In the **Project** tab, configure the **Project SDK** based on your Java JDK and IDEA, by clicking in **new** button; The **Project language level** should be set to **SDK Default** (or to version 8); The **Project compiler output** should be set to a new folder called "out" on the project root folder;
    1. In the **Global Libraries** tab you should create a new library that will contains the following folders (you should adjust it if you have modified, for instance, the IDEA default folder):
        * `C:\Program Files\JetBrains\IntelliJ IDEA {version}\lib`
        * `C:\Program Files\JetBrains\IntelliJ IDEA {version}\plugins\java-i18n\lib`
        * `C:\Program Files\JetBrains\IntelliJ IDEA {version}\plugins\properties\lib`
        * `C:\Program Files\JetBrains\IntelliJ IDEA {version}\plugins\CSS\lib\css-openapi.jar`
        * `C:\Program Files\JetBrains\IntelliJ IDEA {version}\plugins\CSS\lib\css.jar`
        * `C:\Users\{you}\.IntelliJIdea{version}\plugins\blade\lib`
        * `C:\Users\{you}\.IntelliJIdea{version}\plugins\php\lib`
    1. In the **Modules** tab, you should do that:
        * In the **Sources** tab: mark `.idea`, `out` and `development` folder as **excluded**; mark `resources` as **Resources**; mark `resources-tests` as **Test Resources**; mark `src` as **Sources** and the path prefix as `net.rentalhost.idea`;  mark `tests` as **Tests** and the path prefix as `net.rentalhost` only;
        * In the **Dependencies**, add or modify the global library that you have created to **scope *provided***;
    1. This done, you can already compile the project without problems.
1. To make possible to perform the necessary tests on the project, in the **Run / Edit Configurations...** menu, add a new test configuration based on **JUnit**. You should setup it as the follow in the **Configuration** tab:
    1. Set **Test kind** to *All in package*; 
       Set **Package** to `net.rentalhost.idea`;
       Set **Search for tests** to *in single module*;
    1. Set **VM options** to `-ea -Dlog4j.configuration=file:..\log4j.properties`;
       Set **Working directory** to `resources-tests`;
    1. Now you be able to run tests;
