package com.worm.guo.po;

import java.util.List;


public class WormTask {

	private List<Template> template;
	private List<AppChannel> mobileSiteChannels;
	private WebSite site;
	
	public List<Template> getTemplate() {
		return template;
	}
	public void setTemplate(List<Template> templates) {
		this.template = templates;
	}
	public List<AppChannel> getMobileSiteChannels() {
		return mobileSiteChannels;
	}
	public void setMobileSiteChannels(List<AppChannel> mobileSiteChannels) {
		this.mobileSiteChannels = mobileSiteChannels;
	}
	public WebSite getSite() {
		return site;
	}
	public void setSite(WebSite site) {
		this.site = site;
	}
}
