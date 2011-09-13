/**
 * Copyright (c) 2004 - 2011 Eike Stepper (Berlin, Germany) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *    Eike Stepper - initial API and implementation
 */
package org.eclipse.emf.cdo.releng.doc.article.util;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.Doc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.PackageDoc;
import com.sun.javadoc.RootDoc;
import com.sun.javadoc.SourcePosition;
import com.sun.javadoc.Tag;

import java.io.File;
import java.io.IOException;

/**
 * @author Eike Stepper
 */
public final class ArticleUtil
{
  private ArticleUtil()
  {
  }

  public static boolean containsFile(File folder, File file)
  {
    if (!folder.isDirectory())
    {
      return false;
    }

    File parent = file.getParentFile();
    if (parent == null)
    {
      return false;
    }

    if (parent.equals(folder))
    {
      return true;
    }

    return containsFile(folder, parent);
  }

  public static boolean isDocumented(Doc doc)
  {
    Tag[] tags = doc.inlineTags();
    return tags != null && tags.length != 0;
  }

  public static boolean isTagged(Doc doc, String tag)
  {
    Tag[] tags = doc.tags(tag);
    return tags != null && tags.length != 0;
  }

  public static boolean isIgnore(Doc doc)
  {
    return isTagged(doc, "@ignore");
  }

  public static boolean isSnippet(Doc doc)
  {
    return isTagged(doc, "@snippet");
  }

  public static boolean isFactory(MethodDoc doc)
  {
    return isTagged(doc, "@factory");
  }

  public static String getSimplePackageName(PackageDoc packageDoc)
  {
    String name = packageDoc.name();
    int lastDot = name.lastIndexOf('.');
    if (lastDot != -1)
    {
      return name.substring(lastDot + 1);
    }

    return name;
  }

  public static PackageDoc getParentPackage(RootDoc root, PackageDoc packageDoc)
  {
    String name = packageDoc.name();
    int lastDot = name.lastIndexOf('.');
    if (lastDot != -1)
    {
      return root.packageNamed(name.substring(0, lastDot));
    }

    return null;
  }

  public static File canonify(File file)
  {
    try
    {
      return file.getCanonicalFile();
    }
    catch (IOException ex)
    {
      throw new ArticleException(ex);
    }
  }

  public static String makeConsoleLink(Doc doc)
  {
    SourcePosition position = doc.position();
    if (position == null)
    {
      return doc.name();
    }

    return makeConsoleLink(doc, position);
  }

  public static String makeConsoleLink(Doc doc, SourcePosition position)
  {
    if (doc instanceof ClassDoc)
    {
      return makeConsoleLink((ClassDoc)doc, "class", position);
    }

    if (doc instanceof MethodDoc)
    {
      MethodDoc methodDoc = (MethodDoc)doc;
      return makeConsoleLink(methodDoc.containingClass(), methodDoc.name(), position);
    }

    return makeConsoleLink("", position);
  }

  public static String makeConsoleLink(ClassDoc classDoc, String methodName, SourcePosition position)
  {
    String typeName = classDoc.containingPackage().name() + "." + classDoc.typeName().replace('.', '$');
    return makeConsoleLink(typeName + "." + methodName, position);
  }

  public static String makeConsoleLink(String prefix, SourcePosition position)
  {
    String result = position.file().getName() + ":" + position.line();
    if (prefix != null && prefix.length() != 0)
    {
      result = prefix + "(" + result + ")";
    }

    return result;
  }
}