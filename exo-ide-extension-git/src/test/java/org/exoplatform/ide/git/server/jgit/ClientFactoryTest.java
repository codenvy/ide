/*
 * Copyright (C) 2011 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.exoplatform.ide.git.server.jgit;

import org.exoplatform.ide.git.server.GitConnection;
import org.exoplatform.ide.git.server.GitConnectionFactory;
import org.exoplatform.ide.git.shared.InitRequest;

import java.io.File;
import java.net.URL;

/**
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: ClientFactoryTest.java 22811 2011-03-22 07:28:35Z andrew00x $
 */
public class ClientFactoryTest extends BaseTest
{
   public void testClientFactory() throws Exception
   {
      GitConnectionFactory gitClientFactory = GitConnectionFactory.getIntance();

      URL testCls = Thread.currentThread().getContextClassLoader().getResource(".");
      File target = new File(testCls.toURI()).getParentFile();
      File repoDir = new File(target, "ClientFactoryTest");
      forClean.add(repoDir);
      
      GitConnection gitClient = gitClientFactory.getConnection(repoDir);
      
      // Try to initialize repository via obtained client.
      gitClient.init(new InitRequest());
   }
}
