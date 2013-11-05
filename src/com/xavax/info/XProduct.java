//
// Copyright 2004, 2013 by Xavax, Inc. All Rights Reserved.
// Use of this software is allowed under the Xavax Open Software License.
// http://www.xavax.com/xosl.html
//
package com.xavax.info;

/**
 * XProduct encapsulates information about a product including the product name,
 * version, and copyright statement. The copyright statement can be customized
 * by passing a copyright template to the constructor. The default template is a
 * copyright statement common to all Xavax open source products covered by the
 * Xavax Open Source License.
 */
public class XProduct {
  public XProduct(String name, int major, int minor, int patch,
		  int copyrightBegin, int copyrightEnd, String copyright) {
    this.name = name;
    this.major = major;
    this.minor = minor;
    this.patch = patch;
    this.version = null;
    this.copyrightBegin = copyrightBegin;
    this.copyrightEnd = copyrightEnd;
    this.copyright = null;
    this.template = copyright == null ? defaultCopyright : copyright;
  }

  /**
   * Returns a simple banner for the product consisting of the product name,
   * version, and copyright notice.
   * 
   * @return a banner for the product.
   */
  public String banner() {
    return version() + "\n\n" + copyright();
  }

  /**
   * Returns the copyright statement for the product.
   * 
   * @return the copyright statement for the product.
   */
  public String copyright()
  {
    if (copyright == null) {
      String year = String.format(copyrightEnd == 0 ? YEAR : YEAR_SPAN,
	  copyrightBegin, copyrightEnd);
      copyright = String.format(template, year);
    }
    return copyright;
  }

  /**
   * Returns the name of the product.
   *
   * @return the name of the product.
   */
  public String name()
  {
	  return this.name;
  }

  /**
   * Returns the major version number for the product.
   * 
   * @return the major version number for the product.
   */
  public int majorVersion()
  {
    return major;
  }

  /**
   * Returns the minor version number for the product.
   * 
   * @return the minor version number for the product.
   */
  public int minorVersion()
  {
    return minor;
  }

  /**
   * Returns the patch level for the product.
   * 
   * @return the patch level for the product.
   */
  public int patchLevel()
  {
    return patch;
  }

  /**
   * Returns the version for the product as a string.
   * 
   * @return the version for the product as a string.
   */
  public String version()
  {
    if (version == null) {
      version = name + " " + major + "." + minor + "." + patch;
    }
    return version;
  }

  protected final String YEAR = "%1$d";
  protected final String YEAR_SPAN = "%1$d, %2$d";
  protected final String defaultCopyright =
      "Copyright %s by Xavax, Inc. All Rights Reserved.\n"
	  + "Use of this software is allowed under the Xavax Open Software License.\n"
	  + "http://www.xavax.com/xosl.html";

  protected String copyright = null;
  protected String version = null;
  protected final String name;
  protected final String template;
  protected final int copyrightBegin;
  protected final int copyrightEnd;
  protected final int major;
  protected final int minor;
  protected final int patch;
}
