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
package org.exoplatform.ide.core;

import static org.junit.Assert.assertFalse;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * Created by The eXo Platform SAS .
 * 
 * @author <a href="mailto:mmusienko@exoplatform.com">Musienko Maksim</a>
 * @version $
 */

public class FindReplace extends AbstractTestModule
{
   private interface Locators
   {
      String VIEW_LOCATOR = "//div[@view-id='ideFindReplaceTextView']";

      String FIND_BUTTON_ID = "ideFindReplaceTextFormFindButton";

      String REPLACE_BUTTON_ID = "ideFindReplaceTextFormReplaceButton";

      String REPLACE_FIND_BUTTON_ID = "ideFindReplaceTextFormReplaceFindButton";

      String REPLACE_ALL_BUTTON_ID = "ideFindReplaceTextFormReplaceAllButton";

      String FIND_FIELD_ID = "ideFindReplaceTextFormFindField";

      String REPLACE_FIELD_ID = "ideFindReplaceTextFormReplaceField";

      String CASE_SENSITIVE_FIELD_ID = "ideFindReplaceTextFormCaseSensitiveField";

      String FIND_RESULT_LABEL_ID = "ideFindReplaceTextFormFindResult";

   }

   private static final String VIEW_TITLE = "Find/Replace";

   public static final String NOT_FOUND_RESULT = "String not found.";

   @FindBy(xpath = Locators.VIEW_LOCATOR)
   private WebElement view;

   @FindBy(id = Locators.FIND_BUTTON_ID)
   private WebElement findButton;

   @FindBy(id = Locators.REPLACE_BUTTON_ID)
   private WebElement replaceButton;

   @FindBy(id = Locators.REPLACE_FIND_BUTTON_ID)
   private WebElement replaceFindButton;

   @FindBy(id = Locators.REPLACE_ALL_BUTTON_ID)
   private WebElement replaceAllButton;

   @FindBy(name = Locators.FIND_FIELD_ID)
   private WebElement findField;

   @FindBy(name = Locators.REPLACE_FIELD_ID)
   private WebElement replaceField;

   @FindBy(name = Locators.CASE_SENSITIVE_FIELD_ID)
   private WebElement caseSensitiveField;

   @FindBy(id = Locators.FIND_RESULT_LABEL_ID)
   private WebElement resultLabel;

   /**
    * Wait Find/Replace text view opened.
    * 
    * @throws Exception
    */
   public void waitOpened() throws Exception
   {
      new WebDriverWait(driver(), 5).until(new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            try
            {
               return isOpened();
            }
            catch (NoSuchElementException e)
            {
               return false;
            }
         }
      });
   }

   public boolean isOpened()
   {
      return (view != null && view.isDisplayed() && findField != null && findField.isDisplayed()
         && replaceField != null && replaceField.isDisplayed() && findButton != null && findButton.isDisplayed()
         && replaceButton != null && replaceButton.isDisplayed() && replaceFindButton != null
         && replaceFindButton.isDisplayed() && replaceAllButton != null && replaceAllButton.isDisplayed()
         && caseSensitiveField != null && caseSensitiveField.isDisplayed());
   }

   public void waitCloseButtonAppeared() throws Exception
   {
      new WebDriverWait(driver(), 2).until(new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            try
            {
               WebElement closeButton = IDE().PERSPECTIVE.getCloseViewButton(VIEW_TITLE);
               return closeButton != null && closeButton.isDisplayed();
            }
            catch (NoSuchElementException e)
            {
               return false;
            }
         }
      });
   }

   public void waitFindButtonAppeared() throws Exception
   {
      new WebDriverWait(driver(), 2).until(new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            try
            {
               return findButton != null && findButton.isDisplayed();
            }
            catch (NoSuchElementException e)
            {
               return false;
            }
         }
      });
   }

   public void waitFindFieldAppeared() throws Exception
   {
      new WebDriverWait(driver(), 2).until(new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            try
            {
               return findField != null && findField.isDisplayed();
            }
            catch (NoSuchElementException e)
            {
               return false;
            }
         }
      });
   }

   /**
    * Wait Find/Replace text view closed.
    * 
    * @throws Exception
    */
   public void waitClosed() throws Exception
   {
      new WebDriverWait(driver(), 3).until(new ExpectedCondition<Boolean>()
      {
         @Override
         public Boolean apply(WebDriver input)
         {
            try
            {
               return view == null || !view.isDisplayed();
            }
            catch (NoSuchElementException e)
            {
               return true;
            }
         }
      });
   }

   public void closeView()
   {
      IDE().PERSPECTIVE.getCloseViewButton(VIEW_TITLE).click();
   }

   /**
    * Check the state of the find replace form: all elements to be present and the state of buttons.
    */
   @Deprecated
   public void checkFindReplaceFormAppeared()
   {
      // TODO remove to test
      // Check buttons state
      assertFalse(isReplaceButtonEnabled());
      assertFalse(isReplaceFindButtonEnabled());
      assertFalse(isFindButtonEnabled());
      assertFalse(isReplaceAllButtonEnabled());
   }

   /**
    * Get enabled state of find button.
    * 
    * @return boolean enabled state of find button
    */
   public boolean isFindButtonEnabled()
   {
      return IDE().BUTTON.isButtonEnabled(findButton);
   }

   /**
    * Get enabled state of replace button.
    * 
    * @return boolean enabled state of replace button
    */
   public boolean isReplaceButtonEnabled()
   {
      return IDE().BUTTON.isButtonEnabled(replaceButton);
   }

   /**
    * Get enabled state of replace/find button.
    * 
    * @return boolean enabled state of replace/find button
    */
   public boolean isReplaceFindButtonEnabled()
   {
      return IDE().BUTTON.isButtonEnabled(replaceFindButton);
   }

   /**
    * Get enabled state of replace all button.
    * 
    * @return boolean enabled state of replace all button
    */
   public boolean isReplaceAllButtonEnabled()
   {
      return IDE().BUTTON.isButtonEnabled(replaceAllButton);
   }

   public void typeInFindField(String text) throws InterruptedException
   {
      IDE().INPUT.typeToElement(findField, text, true);
   }

   public void typeInReplaceField(String text) throws InterruptedException
   {
      IDE().INPUT.typeToElement(replaceField, text, true);
   }

   public boolean isFindFieldNotEmptyState()
   {
      return (isFindButtonEnabled() && isReplaceAllButtonEnabled() && !isReplaceButtonEnabled() && !isReplaceFindButtonEnabled());
   }

   public boolean checkFindFieldEmptyState()
   {
      return (isFindButtonEnabled() && isReplaceAllButtonEnabled() && !isReplaceButtonEnabled() && !isReplaceFindButtonEnabled());
   }

   public void clickFindButton() throws InterruptedException
   {
      findButton.click();
   }

   public void clickReplaceButton() throws InterruptedException
   {
      replaceButton.click();
   }

   public void clickReplaceFindButton() throws InterruptedException
   {
      replaceFindButton.click();
   }

   public void clickReplaceAllButton() throws InterruptedException
   {
      replaceAllButton.click();
   }

   /**
    * Check buttons when text is found.
    */
   public boolean isStateWhenTextFound()
   {
      return (isFindButtonEnabled() && isReplaceAllButtonEnabled() && isReplaceButtonEnabled()
         && isReplaceFindButtonEnabled() && getFindResultText().isEmpty());
   }

   /**
    * Check buttons when text is not found.
    */
   public boolean isStateWhenTextNotFound()
   {
      return (isFindButtonEnabled() && isReplaceAllButtonEnabled() && !isReplaceButtonEnabled() && !isReplaceFindButtonEnabled())
         && NOT_FOUND_RESULT.equals(getFindResultText());
   }

   /**
    * Returns the value of the find text result label.
    * 
    * @return {@link String} result of the find text operation
    */
   public String getFindResultText()
   {
      return resultLabel.getText();
   }

   /**
    * Click on case sensitive field.
    */
   public void clickCaseSensitiveField()
   {
      caseSensitiveField.click();
   }
}
