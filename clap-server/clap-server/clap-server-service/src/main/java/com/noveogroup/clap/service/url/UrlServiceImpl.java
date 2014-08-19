package com.noveogroup.clap.service.url;

import com.noveogroup.clap.config.ConfigBean;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class UrlServiceImpl implements UrlService {

    private static final String REV_ID_TEMPLATE = "{revId}";
    private static final String VAR_ID_TEMPLATE = "{varId}";
    private static final String TOKEN_TEMPLATE = "{token}";

    @Inject
    private ConfigBean configBean;

    @Override
    public String createUrl(final Long revisionId, final Long variantId, final String token) {
        String url = StringUtils.replace(configBean.getDownloadApkUrl(), REV_ID_TEMPLATE, Long.toString(revisionId));
        url = StringUtils.replace(url, VAR_ID_TEMPLATE, Long.toString(variantId));
        url = StringUtils.replace(url, TOKEN_TEMPLATE, token);
        return url;
    }
}
