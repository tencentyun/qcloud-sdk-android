 
package com.tencent.cos.xml.model.bucket;

import com.tencent.cos.xml.CosXmlService;
import com.tencent.cos.xml.core.TestConst;
import com.tencent.cos.xml.exception.CosXmlClientException;
import com.tencent.cos.xml.exception.CosXmlServiceException;
import com.tencent.cos.xml.listener.CosXmlResultListener;
import com.tencent.cos.xml.model.NormalRequestTestAdapter;
import com.tencent.cos.xml.model.tag.WebsiteConfiguration;

import java.util.ArrayList;
import java.util.List;

// Generate by auto
public class PutBucketWebsiteTestAdapter extends NormalRequestTestAdapter<PutBucketWebsiteRequest, PutBucketWebsiteResult> {
    @Override
    protected PutBucketWebsiteRequest newRequestInstance() {
        PutBucketWebsiteRequest putBucketWebsiteRequest = new PutBucketWebsiteRequest(TestConst.PERSIST_BUCKET);
        putBucketWebsiteRequest.setIndexDocument("index.html");
        putBucketWebsiteRequest.setErrorDocument("error.html");
        putBucketWebsiteRequest.setRedirectAllRequestTo("http");
        List<WebsiteConfiguration.RoutingRule> rules = new ArrayList<>();
        WebsiteConfiguration.RoutingRule routingRule = new WebsiteConfiguration.RoutingRule();
        routingRule.contidion = new WebsiteConfiguration.Contidion();
        routingRule.contidion.keyPrefixEquals="keyPrefix";
        routingRule.contidion.httpErrorCodeReturnedEquals=404;
        routingRule.redirect = new WebsiteConfiguration.Redirect();
        routingRule.redirect.protocol = "https";
//        routingRule.redirect.replaceKeyPrefixWith="replaceKeyPrefix";
        routingRule.redirect.replaceKeyWith="replaceKey";
        rules.add(routingRule);
        WebsiteConfiguration.RoutingRule routingRule1 = new WebsiteConfiguration.RoutingRule();
        routingRule1.contidion = new WebsiteConfiguration.Contidion();
        routingRule1.contidion.keyPrefixEquals="keyPrefix1";
        routingRule1.contidion.httpErrorCodeReturnedEquals=404;
        routingRule1.redirect = new WebsiteConfiguration.Redirect();
        routingRule1.redirect.protocol = "https";
//        routingRule1.redirect.replaceKeyPrefixWith="replaceKeyPrefix1";
        routingRule1.redirect.replaceKeyWith="replaceKey1";
        rules.add(routingRule1);
        putBucketWebsiteRequest.setRoutingRules(rules);
        return putBucketWebsiteRequest;
    }

    @Override
    protected PutBucketWebsiteResult exeSync(PutBucketWebsiteRequest request, CosXmlService cosXmlService) throws CosXmlClientException, CosXmlServiceException {
        return cosXmlService.putBucketWebsite(request);
    }

    @Override
    protected void exeAsync(PutBucketWebsiteRequest request, CosXmlService cosXmlService, CosXmlResultListener resultListener) {
        cosXmlService.putBucketWebsiteAsync(request, resultListener);
    }
}  