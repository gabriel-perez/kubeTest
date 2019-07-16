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
        logger.append(logMessage + "<br>");
    }

    private ApiClient getClient(StringBuilder logger) throws IOException{
        Log(logger,"Preparing Kubernetes client");
        //ApiClient client = Config.defaultClient();
        ApiClient client = Config.fromConfig("kubeconfig.yaml");
        //ApiClient client = Config.fromCluster();
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

            Log(logger, "<table border=\"1px solid black\">");
            Log(logger, "<TR><TH>Pod Name</TH><TH>Container</TH><TH>Status</TH></TR>");

            for (V1Pod item : list.getItems()) {
                for (V1ContainerStatus container : item.getStatus().getContainerStatuses()) {
                    Log(logger, "<TR><TD>" + item.getMetadata().getName() + "</TD><TD>" + container.getName() + "</TD><TD>" + GetStateDescription(container.getState()) + "</TD></TR>");
                }
            }
            Log(logger, "</table>");

        }
        catch (ApiException e) {
            Log(logger,"Error API: " + e.getMessage());
        }
        catch (IOException e) {
            Log(logger,"Error IO: " + e.getMessage());
        }

        return logger.toString();
    }

    @RequestMapping("/listactivepods")
    public String ListActivePods() {

        StringBuilder logger = new StringBuilder();
        Log(logger, "Test Kube v" + version);

        try {

            ApiClient client = getClient(logger);
            CoreV1Api apiInstance = new CoreV1Api();

            Log(logger,"Listing pods");
            //V1PodList list = apiInstance.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
            V1PodList list = apiInstance.listNamespacedPod("krakenprj", null, null, null, null, null, null, null, null, null);

            Log(logger, "<table border=\"1px solid black\">");
            Log(logger, "<TR><TH>Pod Name</TH><TH>Container</TH><TH>Status</TH></TR>");

            for (V1Pod item : list.getItems()) {
                for (V1ContainerStatus container : item.getStatus().getContainerStatuses()) {
                    if (container.getState().getRunning() != null) {
                        Log(logger, "<TR><TD>" + item.getMetadata().getName() + "</TD><TD>" + container.getName() + "</TD><TD>" + GetStateDescription(container.getState()) + "</TD></TR>");
                    }
                }
            }

            Log(logger, "</table>");
        }
        catch (ApiException e) {
            Log(logger,"Error API: " + e.getMessage());
        }
        catch (IOException e) {
            Log(logger,"Error IO: " + e.getMessage());
        }

        return logger.toString();
    }

    private String GetStateDescription(V1ContainerState state){
        if (state.getRunning() != null)
            return "Running";
        else if(state.getWaiting() != null)
            return "Waiting";
        else if(state.getTerminated() != null)
            return "Terminated";
        else
            return "Unknow";
    }

    @RequestMapping(value="killpod", method = RequestMethod.GET)
    public String KillPod(@RequestParam("podName") String podName) {

        StringBuilder logger = new StringBuilder();
        Log(logger, "Test Kube v" + version);

        try {

            ApiClient client = getClient(logger);
            CoreV1Api apiInstance = new CoreV1Api();

            Log(logger,"Killing pods");

            V1DeleteOptions options = new V1DeleteOptions();

            V1Status result = apiInstance.deleteNamespacedPod (podName, "krakenprj", options,null, null, null, null, null);

            Log(logger, "Kill pods result: " + result.getMessage());

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

        //Currently forbiden

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

}
