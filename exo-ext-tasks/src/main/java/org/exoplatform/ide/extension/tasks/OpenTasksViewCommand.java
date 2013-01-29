/*
 * Copyright (C) 2003-2013 eXo Platform SAS.
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
package org.exoplatform.ide.extension.tasks;

import com.google.inject.Singleton;

import com.google.gwt.resources.client.ImageResource;
import com.google.inject.Inject;

import org.exoplatform.ide.api.ui.workspace.WorkspaceAgent;
import org.exoplatform.ide.core.expressions.Expression;
import org.exoplatform.ide.extension.tasks.part.TasksPartPresenter;
import org.exoplatform.ide.menu.ExtendedCommand;
import org.exoplatform.ide.perspective.PerspectivePresenter.PartStackType;

/**
 * Demo command that opens the Tasks View Part
 * 
 * @author <a href="mailto:nzamosenchuk@exoplatform.com">Nikolay Zamosenchuk</a>
 */
@Singleton
public class OpenTasksViewCommand implements ExtendedCommand
{
   /**
    * 
    */
   private final TasksPartPresenter tasksPartPresenter;

   /**
    * 
    */
   private final WorkspaceAgent agent;

   /**
    * @param tasksPartPresenter
    * @param agent
    */
   @Inject
   OpenTasksViewCommand(TasksPartPresenter tasksPartPresenter, WorkspaceAgent agent)
   {
      this.tasksPartPresenter = tasksPartPresenter;
      this.agent = agent;
   }

   @Override
   public Expression inContext()
   {
      return null;
   }

   @Override
   public String getToolTip()
   {
      return "Open the view with tasks";
   }

   @Override
   public ImageResource getIcon()
   {
      return null;
   }

   @Override
   public void execute()
   {
      agent.showPart(tasksPartPresenter, PartStackType.TOOLING);
   }

   @Override
   public Expression canExecute()
   {
      return null;
   }
}