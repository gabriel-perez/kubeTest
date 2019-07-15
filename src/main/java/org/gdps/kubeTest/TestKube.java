package org.gdps.kubeTest;

import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.ApiException;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.JSON;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.*;
import io.kubernetes.client.util.Config;
import io.kubernetes.client.auth.*;
import io.kubernetes.client.apis.CoreApi;

import java.io.IOException;

@RestController
@EnableAutoConfiguration
public class TestKube {

    @RequestMapping("/")
    public String Test() {

        return "Testing kube";
    }

    @RequestMapping("/kube")
    public String TesKube() {

        String message = "";
        try {

            System.out.println("Preparing Kubernetes client");
            message += "Preparing Kubernetes client";

            //ApiClient client = Config.defaultClient();
            //ApiClient client = Config.fromConfig("resources/kubeconfig.yaml");
            ApiClient client = Config.fromCluster();
            //ApiClient client = Config.fromToken("https://api.us-west-2.online-starter.openshift.com:6443/apis/user.openshift.io/v1/users/~","Authorization: Bearer fTRF8_hYSN07rsx3M5Kh0x3VXK2u7__zZT961HcqUvE");

            //ApiKeyAuth BearerToken = (ApiKeyAuth) client.getAuthentication("BearerToken");
            //BearerToken.setApiKey("oNRbGCqK_rPKWK097baPK9mF5fA4sVA8cnISMQguNcg");
            //BearerToken.setApiKeyPrefix("Bearer");

            Configuration.setDefaultApiClient(client);
            CoreV1Api apiInstance = new CoreV1Api();

            /*System.out.println("Listing authentications");
            Iterator var2 = client.getAuthentications().values().iterator();

            Authentication auth;
            while (var2.hasNext()) {
                 auth = (Authentication)var2.next();
                System.out.println(auth.toString());


                if (auth instanceof ApiKeyAuth)
                {
                    ApiKeyAuth apiAuth = (ApiKeyAuth)auth;
                    System.out.println(apiAuth.getLocation());
                }
            }*/

            //System.out.println("Listing configs");
            //apiInstance.listNamespacedConfigMap("krakenprj", false, "true", null, null, null, 60, null, 60, false);


            /*System.out.println("Listing namespaces");
            //V1PodList list = apiInstance.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
            V1NamespaceList nslist = apiInstance.listNamespace(null, null, null, null, null, null, null, null, null);

            for (V1Namespace item : nslist.getItems()) {
                System.out.println(item.getMetadata().getName());
            }*/

            System.out.println("Listing pods");
            //V1PodList list = apiInstance.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
            V1PodList list = apiInstance.listNamespacedPod("krakenprj", null, null, null, null, null, null, null, null, null);

            for (V1Pod item : list.getItems()) {
                message += item.getMetadata().getName();
            }
        }
        catch (ApiException e) {
            message += "Error API trying to list pods: \r\n" + e.getMessage();
        }
        catch (IOException e) {
            message += "Error IO trying to list pods: \r\n" + e.getMessage();
        }

        return message;

    }
}
