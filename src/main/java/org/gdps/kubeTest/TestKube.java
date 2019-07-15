package org.gdps.kubeTest;

import org.springframework.boot.autoconfigure.*;
import org.springframework.web.bind.annotation.*;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

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
    int version = 3;

    @RequestMapping("/")
    public String Test() {

        return "Testing kube " + version;
    }

    private void Log(StringBuilder logger, String logMessage)
    {
        logger.append(logMessage + "\r\n");
    }

    private ApiClient getClient(StringBuilder logger) throws IOException{
        Log(logger,"Preparing Kubernetes client");
        //ApiClient client = Config.defaultClient();
        //ApiClient client = Config.fromConfig("resources/kubeconfig.yaml");
        ApiClient client = Config.fromCluster();
        //ApiClient client = Config.fromToken("https://api.us-west-2.online-starter.openshift.com:6443/apis/user.openshift.io/v1/users/~","Authorization: Bearer fTRF8_hYSN07rsx3M5Kh0x3VXK2u7__zZT961HcqUvE");

        ApiKeyAuth BearerToken = (ApiKeyAuth) client.getAuthentication("BearerToken");
        BearerToken.setApiKey("oNRbGCqK_rPKWK097baPK9mF5fA4sVA8cnISMQguNcg");
        //BearerToken.setApiKeyPrefix("Bearer");
        Configuration.setDefaultApiClient(client);

        return client;
    }

    @RequestMapping("/listpods")
    public String ListPods() {

        StringBuilder logger = new StringBuilder();
        Log(logger, "Test Kube v" + version);

        try {

            ApiClient client = getClient(logger);
            CoreV1Api apiInstance = new CoreV1Api();

            Log(logger,"Listing pods");
            //V1PodList list = apiInstance.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
            V1PodList list = apiInstance.listNamespacedPod("krakenprj", null, null, null, null, null, null, null, null, null);

            for (V1Pod item : list.getItems()) {
                Log(logger, "Pod instance: " + item.getMetadata().getName());
            }
        }
        catch (ApiException e) {
            Log(logger,"Error API: " + e.getMessage());
        }
        catch (IOException e) {
            Log(logger,"Error IO: " + e.getMessage());
        }

        return logger.toString();
    }

    @RequestMapping("/listpods2")
    public String ListPods2() {

        StringBuilder logger = new StringBuilder();
        Log(logger, "Test Kube v" + version);

        try {

            ApiClient client = getClient(logger);
            CoreV1Api apiInstance = new CoreV1Api();

            Log(logger,"Listing pods");
            //V1PodList list = apiInstance.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
            V1PodList list = apiInstance.listNamespacedPod("default", null, null, null, null, null, null, null, null, null);

            for (V1Pod item : list.getItems()) {
                Log(logger, "Pod instance: " + item.getMetadata().getName());
            }
        }
        catch (ApiException e) {
            Log(logger,"Error API: " + e.getMessage());
        }
        catch (IOException e) {
            Log(logger,"Error IO: " + e.getMessage());
        }

        return logger.toString();
    }

    @RequestMapping("/listnamespaces")
    public String ListNamespaces() {

        StringBuilder logger = new StringBuilder();
        Log(logger, "Test Kube v" + version);

        try {

            ApiClient client = getClient(logger);
            CoreV1Api apiInstance = new CoreV1Api();

            Log(logger,"Listing namespaces");
            V1NamespaceList nslist = apiInstance.listNamespace(null, null, null, null, null, null, null, null, null);

            for (V1Namespace item : nslist.getItems()) {
                Log(logger, "Namespace: " + item.getMetadata().getName());
            }

        }
        catch (ApiException e) {
            Log(logger,"Error API: " + e.getMessage());
        }
        catch (IOException e) {
            Log(logger,"Error IO: " + e.getMessage());
        }

        return logger.toString();
    }

    @RequestMapping("/listauthentications")
    public String ListAuthentications() {

        StringBuilder logger = new StringBuilder();
        Log(logger, "Test Kube v" + version);

        try {

            ApiClient client = getClient(logger);
            CoreV1Api apiInstance = new CoreV1Api();

            System.out.println("Listing authentications");
            Iterator var2 = client.getAuthentications().values().iterator();

            Authentication auth;
            while (var2.hasNext()) {
                 auth = (Authentication)var2.next();
                Log(logger, auth.toString());


                if (auth instanceof ApiKeyAuth)
                {
                    ApiKeyAuth apiAuth = (ApiKeyAuth)auth;
                    Log(logger, apiAuth.getLocation());
                }
            }
        }
        catch (IOException e) {
            Log(logger,"Error IO: " + e.getMessage());
        }

        return logger.toString();
    }

    @RequestMapping("/listconfig")
    public String ListConfigMap() {

        StringBuilder logger = new StringBuilder();
        Log(logger, "Test Kube v" + version);

        try {

            ApiClient client = getClient(logger);
            CoreV1Api apiInstance = new CoreV1Api();

            Log(logger,"Listing Configs");
            V1ConfigMapList nslist = apiInstance.listNamespacedConfigMap("krakenprj", null, null, null, null, null, null, null, null, null);

            for (V1ConfigMap item : nslist.getItems()) {
                Log(logger, "Config: " + item.getMetadata().getName());
            }

        }
        catch (ApiException e) {
            Log(logger,"Error API: " + e.getMessage());
        }
        catch (IOException e) {
            Log(logger,"Error IO: " + e.getMessage());
        }

        return logger.toString();
    }
}
