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
package org.exoplatform.ide.git.shared;

/**
 * Request to commit current state of index in new commit.
 * 
 * @author <a href="mailto:andrey.parfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: CommitRequest.java 22811 2011-03-22 07:28:35Z andrew00x $
 */
public class CommitRequest extends GitRequest
{
   /**
    * Commit message.
    */
   private String message;

   /**
    * @param user commit author
    * @param message commit message
    */
   public CommitRequest(String message, GitUser user)
   {
      this.message = message;
      setUser(user);
   }

   /**
    * "Empty" commit request. Corresponding setters used to setup required
    * parameters.
    */
   public CommitRequest()
   {
   }

   /**
    * @return commit message
    */
   public String getMessage()
   {
      return message;
   }

   /**
    * @param message commit message
    */
   public void setMessage(String message)
   {
      this.message = message;
   }
}
