/*
 * This file is part of VisualVM for IDEA
 *
 * Copyright (c) 2008, Esko Luontola. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *     * Redistributions of source code must retain the above copyright notice,
 *       this list of conditions and the following disclaimer.
 *
 *     * Redistributions in binary form must reproduce the above copyright notice,
 *       this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *
 *     * Neither the name of the copyright holder nor the names of its contributors
 *       may be used to endorse or promote products derived from this software
 *       without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package net.orfjackal.visualvm4idea.plugin.server;

import net.orfjackal.visualvm4idea.plugin.PluginSettingsComponent;

/**
 * @author Esko Luontola
 * @since 6.11.2008
 */
public class VisualVmUtil {

    private VisualVmUtil() {
    }

    private static VisualVmConfig getConfig() {
        String visualVmHome = PluginSettingsComponent.getInstance().getVisualVmHome();
        return new WindowsExternalVisualVmConfig(visualVmHome);
    }

    public static String getAppProfilerCommand(JdkVersion jdkVersion) {
        VisualVmConfig config = getConfig();
        String agent = config.getAppProfilerAgent(jdkVersion);
        String lib = config.getAppProfilerLib();
        return "-agentpath:" + agent + "=" + lib + "," + VisualVmCommandSender.PROFILER_PORT;
    }

    public static String getVisualVmExecutable() {
        return getConfig().getVisualVmExecutable();
    }

    public static String getVisualVmHookAgent() {
        return getConfig().getVisualVmHookAgent();
    }

    public static String getVisualVmHookLib() {
        return getConfig().getVisualVmHookLib();
    }
}
