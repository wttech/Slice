package com.cognifide.slice.api.tag;

import javax.servlet.jsp.tagext.TagData;
import javax.servlet.jsp.tagext.TagExtraInfo;
import javax.servlet.jsp.tagext.VariableInfo;

/**
 * User: krzysztof.ryk@solsoft.pl
 * Date: 11/7/13 1:07 PM
 */
public class SliceLookupTei extends TagExtraInfo {

   private static final String ATTRIBUTE_CLS = "cls";
   private static final String ATTRIBUTE_VAR = "var";
   private static final String DEFAULT_TYPE = "java.lang.Object";

   /**
    * Get variable info for controller tag. This method adds additional info to object placed in the page context.
    * In general, this additional info references to model class. This approach allows us to use Code Completion feature
    * in our IDE's (works in Idea IntelliJ, not tested on Eclipse)
    *
    * @param data Tag data
    * @return Variable infos - info containing model class
    */
   @Override
   public VariableInfo[] getVariableInfo(TagData data) {
      String cls = data.getAttributeString(ATTRIBUTE_CLS);
      if (cls == null) {
         cls = DEFAULT_TYPE;
      }
      return new VariableInfo[]{
            new VariableInfo(data.getAttributeString(ATTRIBUTE_VAR), cls, true, VariableInfo.AT_BEGIN)};
   }

}
