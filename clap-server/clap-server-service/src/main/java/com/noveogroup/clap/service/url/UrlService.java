package com.noveogroup.clap.service.url;

public interface UrlService {
    String createUrl(Long revisionId, boolean isMainPackage, String token);
}
