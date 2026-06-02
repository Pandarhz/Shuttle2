package com.simplecity.amp_library.ui.screens.drawer;

public final class LockUtils {

    private LockUtils() {
        // utility class
    }

    public static void addDrawerLock(DrawerLockManager.DrawerLock drawerLock) {
        DrawerLockManager.getInstance().addDrawerLock(drawerLock);
    }

    public static void removeDrawerLock(DrawerLockManager.DrawerLock drawerLock) {
        DrawerLockManager.getInstance().removeDrawerLock(drawerLock);
    }

    public static void addDrawerAndMiniPlayerLock(DrawerLockManager.DrawerLock drawerLock, MiniPlayerLockManager.MiniPlayerLock miniPlayerLock) {
        DrawerLockManager.getInstance().addDrawerLock(drawerLock);
        MiniPlayerLockManager.getInstance().addMiniPlayerLock(miniPlayerLock);
    }

    public static void removeDrawerAndMiniPlayerLock(DrawerLockManager.DrawerLock drawerLock, MiniPlayerLockManager.MiniPlayerLock miniPlayerLock) {
        DrawerLockManager.getInstance().removeDrawerLock(drawerLock);
        MiniPlayerLockManager.getInstance().removeMiniPlayerLock(miniPlayerLock);
    }
}
