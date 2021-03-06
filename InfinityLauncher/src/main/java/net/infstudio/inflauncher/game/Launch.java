/*
 * Infinity Launcher for Minecraft.
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package net.infstudio.inflauncher.game;

import net.infstudio.inflauncher.InfinityLauncher;
import net.infstudio.inflauncher.config.LaunchConfig;
import org.to2mbn.jmccc.auth.Authenticator;
import org.to2mbn.jmccc.auth.OfflineAuthenticator;
import org.to2mbn.jmccc.launch.LaunchException;
import org.to2mbn.jmccc.launch.Launcher;
import org.to2mbn.jmccc.launch.LauncherBuilder;
import org.to2mbn.jmccc.launch.ProcessListener;
import org.to2mbn.jmccc.option.LaunchOption;
import org.to2mbn.jmccc.option.MinecraftDirectory;

import java.io.IOException;

public class Launch {

    public static void launchVanilla(LaunchOption option) {
        try {
            Launcher launcher = LauncherBuilder.buildDefault();
            launcher.launch(option, new ProcessListener() {
                @Override
                public void onLog(String log) {
                    InfinityLauncher.LOGGER.info(log);
                }

                @Override
                public void onErrorLog(String log) {
                    InfinityLauncher.LOGGER.error(log);
                }

                @Override
                public void onExit(int code) {
                    InfinityLauncher.LOGGER.info("Minecraft exited with return value " + String.valueOf(code));
                }
            });
        } catch (LaunchException e) {
            InfinityLauncher.LOGGER.error("Error when launching", e);
        }
    }

    public static void launchVanilla(LaunchConfig config) {
        MinecraftDirectory directory = new MinecraftDirectory(config.getMinecraftDirectory());
        Authenticator authenticator;
        try {
            int authenticatorId = config.getAuthenticator();
            switch (authenticatorId) {
                case 0:
                    authenticator = new OfflineAuthenticator(config.getName());
                    break;
                default:
                    throw new NoSuchAuthenticatorException(authenticatorId);
            }
        } catch (NoSuchAuthenticatorException e) {
            InfinityLauncher.LOGGER.error(e);
            authenticator = new OfflineAuthenticator(config.getName());
        }
        try {
            LaunchOption option = new LaunchOption(config.getVersion(), authenticator, directory);
            option.setMaxMemory(config.getMemory());
            option.setWindowSize(config.getWindowSize());
            Launch.launchVanilla(option);
        } catch (IOException e) {
            InfinityLauncher.LOGGER.error(e);
        }
    }
}
