package org.my.automationtest.locator;

import org.openqa.selenium.By;

public class WebLocator {

    public WebLocator() {
        // Default constructor
    }

    public WebLocator(String id) {
        id(id);
    }

    private String alias;

    private String name;
    private String id;
    private String tag;
    private String href;
    private String className;
    private String innerText;
    private String type;

    private String xpath;
    private String cssSelector;

    public boolean isAnyElementAttributeProvided() {
        return getName() != null || getId() != null || getTag() != null || getHref() != null || getInnerText() != null || getClassName() != null || getType() != null;
    }

    public By toBy() {
        By by;
        String generatedXpath = toXpath();
        if (generatedXpath != null) {
            by = By.xpath(generatedXpath);
        } else if (getCssSelector() != null) {
            by = By.cssSelector(getCssSelector());
        } else {
            throw  new RuntimeException("There is no web element checking condition defined in Locator object");
        }
        return by;
    }

    public String toXpath() {
        if (getXpath() != null) {
            return getXpath();
        } else if (isAnyElementAttributeProvided()) {
            StringBuilder sb = new StringBuilder();
            if (getTag() == null) {
                sb.append("//*");
            } else {
                sb.append("//").append(getTag());
            }
            String _and = "[";
            if (getId() != null) {
                sb.append(_and).append("@id='").append(getId()).append("'");
                _and = " and ";
            }
            if (getName() != null) {
                sb.append(_and).append("@name='").append(getName()).append("'");
                _and = " and ";
            }
            if (getHref() != null) {
                sb.append(_and).append("@href='").append(getHref()).append("'");
                _and = " and ";
            }
            if (getClassName() != null) {
                sb.append(_and).append("@class='").append(getClassName()).append("'");
                _and = " and ";
            }
            if (getInnerText() != null) {
                sb.append(_and).append("text()='").append(getInnerText()).append("'");
                _and = " and ";
            }
            if (getType() != null) {
                sb.append(_and).append("@type='").append(getType()).append("'");
                _and = " and ";
            }
            if (" and ".equals(_and)) {
                sb.append("]");
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    public String getAlias() {
        return alias;
    }

    public WebLocator alias(String alias) {
        this.alias = alias;
        return this;
    }

    public String getName() {
        return name;
    }

    public WebLocator name(String name) {
        this.name = name;
        return this;
    }

    public String getId() {
        return id;
    }

    public WebLocator id(String id) {
        this.id = id;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public WebLocator tag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getHref() {
        return href;
    }

    public WebLocator href(String href) {
        this.href = href;
        return this;
    }

    public String getClassName() {
        return className;
    }

    public WebLocator className(String className) {
        this.className = className;
        return this;
    }

    public String getInnerText() {
        return innerText;
    }

    public WebLocator innerText(String innerText) {
        this.innerText = innerText;
        return this;
    }

    public String getType() {
        return type;
    }

    public WebLocator type(String type) {
        this.type = type;
        return this;
    }

    public String getXpath() {
        return xpath;
    }

    public WebLocator xpath(String xpath) {
        this.xpath = xpath;
        return this;
    }

    public String getCssSelector() {
        return cssSelector;
    }

    public WebLocator cssSelector(String cssSelector) {
        this.cssSelector = cssSelector;
        return this;
    }

    @Override
    public String toString() {
        if (toXpath() != null) {
            return toXpath();
        } else if (getCssSelector() != null) {
            return getCssSelector();
        } else {
            return super.toString();
        }
    }
}
