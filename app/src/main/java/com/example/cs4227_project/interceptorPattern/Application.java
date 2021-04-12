package com.example.cs4227_project.interceptorPattern;

public class Application {

    public static Framework buildFramework() {
        return new Framework();
    }

    public static void registerDispatcher(Context context) {
        Framework.getInstance().dispatcherList.add(new Dispatcher(context));
    }

    public static void removeDispatcher(Context context) {
        for (Dispatcher d : Framework.getInstance().dispatcherList) {
            if(d.getContextType() == context.getClass()) {
                Framework.getInstance().dispatcherList.remove(d);
                break;
            }
        }
    }
}
