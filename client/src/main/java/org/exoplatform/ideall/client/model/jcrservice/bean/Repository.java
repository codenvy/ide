/*
 * Copyright (C) 2003-2007 eXo Platform SAS.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Affero General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see<http://www.gnu.org/licenses/>.
 */
package org.exoplatform.ideall.client.model.jcrservice.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:gavrikvetal@gmail.com">Vitaliy Gulyy</a>
 * @version @version $Id: $
 */

public class Repository
{

   private String name;

   private String systemWorkspaceName;

   private List<Workspace> workspaces = new ArrayList<Workspace>();

   /**
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * @param name the name to set
    */
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * @return the systemWorkspaceName
    */
   public String getSystemWorkspaceName()
   {
      return systemWorkspaceName;
   }

   /**
    * @param systemWorkspaceName the systemWorkspaceName to set
    */
   public void setSystemWorkspaceName(String systemWorkspaceName)
   {
      this.systemWorkspaceName = systemWorkspaceName;
   }

   /**
    * @return the workspaces
    */
   public List<Workspace> getWorkspaces()
   {
      return workspaces;
   }

}
