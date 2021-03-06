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

import com.intellij.openapi.diagnostic.Logger;
import net.orfjackal.visualvm4idea.comm.*;
import net.orfjackal.visualvm4idea.core.commands.*;
import net.orfjackal.visualvm4idea.visualvm.*;

import java.util.Arrays;
import java.util.concurrent.*;

/**
 * @author Esko Luontola
 * @since 17.10.2008
 */
public class VisualVmCommandSender {
    private static final Logger log = Logger.getInstance(VisualVmCommandSender.class.getName());

    public static final int PROFILER_PORT = 5140;

    private static VisualVmCommandSender instance;
    private final MessageSender visualvm;

    public synchronized static VisualVmCommandSender getInstance() {
        if (instance == null) {
            instance = new VisualVmCommandSender();
        }
        return instance;
    }

    public VisualVmCommandSender() {
        visualvm = new MessageServer(new VisualVmLauncher());
    }

    public void beginProfilingApplicationCPU(int appUniqueId, int profilerPort, boolean profileNewThreads, String roots,
                                             CpuSettings.FilterType filterType, String filter) {
        ProfileCpuCommand cmd = new ProfileCpuCommand();
        cmd.appUniqueId = appUniqueId;
        cmd.profilerPort = profilerPort;
        cmd.profileNewThreads = profileNewThreads;
        cmd.roots = roots;
        cmd.filterType = filterType;
        cmd.filter = filter;
        runCommand(cmd);
    }

    public void beginProfilingApplicationMemory(int appUniqueId, int profilerPort, MemorySettings.AllocMode allocMode,
                                                int allocInterval, boolean recordAllocTraces) {
        ProfileMemoryCommand cmd = new ProfileMemoryCommand();
        cmd.appUniqueId = appUniqueId;
        cmd.profilerPort = profilerPort;
        cmd.allocMode = allocMode;
        cmd.allocInterval = allocInterval;
        cmd.recordAllocTraces = recordAllocTraces;
        runCommand(cmd);
    }

    private String[] runCommand(Command command) {
        String[] message = command.toMessage();
        try {
            log.info("Sent request " + Arrays.toString(message));
            String[] response = visualvm.send(message).get(10, TimeUnit.SECONDS);
            log.info("Got response " + Arrays.toString(response) + " to request " + Arrays.toString(message));
            return response;

        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted while waiting for response, request was: " + Arrays.toString(message), e);
        } catch (ExecutionException e) {
            throw new RuntimeException("Remote exception on VisualVM, request was: " + Arrays.toString(message), e);
        } catch (TimeoutException e) {
            throw new RuntimeException("No response from VisualVM, request was: " + Arrays.toString(message), e);
            // TODO: Instead of throwing an exception, show a message dialog asking if the user wants to stop waiting.
            // If the response does arrive before the user stops waiting, hide the dialog and resume normally.
        }
    }
}
