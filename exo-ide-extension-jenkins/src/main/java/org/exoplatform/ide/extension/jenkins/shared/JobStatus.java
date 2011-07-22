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
package org.exoplatform.ide.extension.jenkins.shared;

/**
 * @author <a href="mailto:aparfonov@exoplatform.com">Andrey Parfonov</a>
 * @version $Id: $
 */
public class JobStatus
{
   public enum Status {
      QUEUE("in queue"), //
      BUILD("building"), //
      END("end"); //

      private final String value;

      private Status(String value)
      {
         this.value = value;
      }

      @Override
      public String toString()
      {
         return value;
      }
   }

   /** Job name. */
   private String name;

   /** Current job status. */
   private Status status;

   /** Result of last build. Should be always <code>null</code> if {@link #status} other then {@link Status#END}. */
   private String lastBuildResult;

   /**
    * URL to download artifact. Should be always <code>null</code> if {@link #status} other then {@link Status#END} and
    * {@link #lastBuildResult} other then 'SUCCESS'.
    */
   private String artifactUrl;

   /** URL to get console output. Should be always <code>null</code> if {@link #status} other then {@link Status#END}. */
   private String outputUrl;

   public JobStatus(String name, Status status, String lastBuildResult, String outputUrl, String artifactUrl)
   {
      this.name = name;
      this.status = status;
      this.lastBuildResult = lastBuildResult;
      this.outputUrl = outputUrl;
      this.artifactUrl = artifactUrl;
   }

   public JobStatus()
   {
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public Status getStatus()
   {
      return status;
   }

   public void setStatus(Status status)
   {
      this.status = status;
   }

   public String getLastBuildResult()
   {
      return lastBuildResult;
   }

   public void setLastBuildResult(String lastBuildResult)
   {
      this.lastBuildResult = lastBuildResult;
   }

   public String getOutputUrl()
   {
      return outputUrl;
   }

   public void setOutputUrl(String outputUrl)
   {
      this.outputUrl = outputUrl;
   }

   public String getArtifactUrl()
   {
      return artifactUrl;
   }

   public void setArtifactUrl(String artifactUrl)
   {
      this.artifactUrl = artifactUrl;
   }

   @Override
   public String toString()
   {
      return "JobStatus [name=" + name + ", status=" + status + ", lastBuildResult=" + lastBuildResult
         + ", artifactUrl=" + artifactUrl + ", outputUrl=" + outputUrl + "]";
   }
}
