/**
 * Neociclo Accord, Open Source B2B Integration Suite
 * Copyright (C) 2005-2012 Neociclo, http://www.neociclo.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Id$
 */
package org.neociclo.odetteftp.examples.proxy;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandler;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.MessageEvent;
import org.neociclo.capi20.message.MessageType;

/**
 * @author Rafael Marins
 * @version $Rev$ $Date$
 */
public interface RemoteCapiMessageProcessor {

	boolean accept(ChannelHandler source, MessageType messageType);

	ChannelBuffer process(ChannelHandlerContext ctx, MessageEvent e);

}
