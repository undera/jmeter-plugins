package kg.apc.jmeter.modifiers;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.util.*;
import org.apache.jmeter.config.Argument;
import org.apache.jmeter.config.Arguments;
import org.apache.jmeter.processor.PreProcessor;
import org.apache.jmeter.protocol.http.parser.HtmlParsingUtils;
import org.apache.jmeter.protocol.http.sampler.HTTPSampleResult;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.protocol.http.util.ConversionUtils;
import org.apache.jmeter.protocol.http.util.HTTPConstants;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.samplers.Sampler;
import org.apache.jmeter.testelement.AbstractTestElement;
import org.apache.jmeter.testelement.property.JMeterProperty;
import org.apache.jmeter.testelement.property.PropertyIterator;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jorphan.logging.LoggingManager;
import org.apache.log.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

// just copied it from JMeter, but added single global map
public class AnchorModifier extends AbstractTestElement implements PreProcessor, Serializable {

    private static final Logger log = LoggingManager.getLoggerForClass();
    private static final Random rand = new Random();
    private final static Boolean visited = true;
    private final static Boolean notvisited = false;
    List<String> marks = new LinkedList<String>();
    private HashMap<String, HTTPSamplerBase> URLs = new HashMap<String, HTTPSamplerBase>();
    private String baseDomain;

    public AnchorModifier() {
    }

    /**
     * Modifies an Entry object based on HTML response text.
     */
    public void process() {
        JMeterContext context = getThreadContext();
        Sampler sam = context.getCurrentSampler();
        SampleResult res = context.getPreviousResult();
        HTTPSamplerBase sampler;
        HTTPSampleResult result;
        if (res == null || !(sam instanceof HTTPSamplerBase) || !(res instanceof HTTPSampleResult)) {
            log.info("Can't apply HTML Link Parser when the previous" + " sampler run is not an HTTP Request.");
            return;
        } else {
            sampler = (HTTPSamplerBase) sam;
            result = (HTTPSampleResult) res;
        }
        String responseText; // $NON-NLS-1$
        responseText = result.getResponseDataAsString();
        Document html;
        int index = responseText.indexOf("<"); // $NON-NLS-1$
        if (index == -1) {
            index = 0;
        }
        /*
         * if (log.isDebugEnabled()) { log.debug("Check for matches against: " +
         * sampler.toString()); }
         *
         */
        html = (Document) HtmlParsingUtils.getDOM(responseText.substring(index));
        addAnchorUrls(html, result, sampler);
        addFormUrls(html, result, sampler);
        addFramesetUrls(html, result, sampler);
        if (hasNotVisited()) {
            HTTPSamplerBase url = getNextLink();
            /*
             * if (log.isDebugEnabled()) { log.debug("Selected: " +
             * url.toString()); }
             *
             */
            sampler.setDomain(url.getDomain());
            sampler.setPath(url.getPath());
            if (url.getMethod().equals(HTTPConstants.POST)) {
                PropertyIterator iter = sampler.getArguments().iterator();
                while (iter.hasNext()) {
                    Argument arg = (Argument) iter.next().getObjectValue();
                    modifyArgument(arg, url.getArguments());
                }
            } else {
                sampler.setArguments(url.getArguments());
                // config.parseArguments(url.getQueryString());
            }
            sampler.setProtocol(url.getProtocol());
        } else {
            log.info("No further matches found, stopping test");
            context.getEngine().askThreadsToStop();
        }
    }

    private void modifyArgument(Argument arg, Arguments args) {
        /*
         * if (log.isDebugEnabled()) { log.debug("Modifying argument: " + arg);
         * }
         *
         */
        List<Argument> possibleReplacements = new ArrayList<Argument>();
        PropertyIterator iter = args.iterator();
        Argument replacementArg;
        while (iter.hasNext()) {
            replacementArg = (Argument) iter.next().getObjectValue();
            try {
                if (HtmlParsingUtils.isArgumentMatched(replacementArg, arg)) {
                    possibleReplacements.add(replacementArg);
                }
            } catch (Exception ex) {
                log.error("Problem adding Argument", ex);
            }
        }

        if (possibleReplacements.size() > 0) {
            replacementArg = possibleReplacements.get(rand.nextInt(possibleReplacements.size()));
            arg.setName(replacementArg.getName());
            arg.setValue(replacementArg.getValue());
            /*
             * if (log.isDebugEnabled()) { log.debug("Just set argument to
             * values: " + arg.getName() + " = " + arg.getValue()); }
             *
             */
            args.removeArgument(replacementArg);
        }
    }

    private void addFormUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config) {
        NodeList rootList = html.getChildNodes();
        List<HTTPSamplerBase> urls = new LinkedList<HTTPSamplerBase>();
        for (int x = 0; x < rootList.getLength(); x++) {
            urls.addAll(HtmlParsingUtils.createURLFromForm(rootList.item(x), result.getURL()));
        }

        for (Iterator<HTTPSamplerBase> it = urls.iterator(); it.hasNext();) {
            HTTPSamplerBase newUrl = it.next();
            newUrl.setMethod(HTTPConstants.POST);
            /*
             * if (log.isDebugEnabled()) { log.debug("Potential Form match: " +
             * newUrl.toString()); }
             *
             */
            if (HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
                //log.debug("Matched!");
                addURL(newUrl);
            }
        }
    }

    private void addAnchorUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config) {
        String base = "";
        NodeList baseList = html.getElementsByTagName("base"); // $NON-NLS-1$
        if (baseList.getLength() > 0) {
            base = baseList.item(0).getAttributes().getNamedItem("href").getNodeValue(); // $NON-NLS-1$
        }
        NodeList nodeList = html.getElementsByTagName("a"); // $NON-NLS-1$
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            NamedNodeMap nnm = tempNode.getAttributes();
            Node namedItem = nnm.getNamedItem("href"); // $NON-NLS-1$
            if (namedItem == null) {
                continue;
            }
            String hrefStr = namedItem.getNodeValue();
            if (hrefStr.startsWith("javascript:")) { // $NON-NLS-1$
                continue; // No point trying these
            }
            try {
                HTTPSamplerBase newUrl = HtmlParsingUtils.createUrlFromAnchor(hrefStr, ConversionUtils.makeRelativeURL(result.getURL(), base));
                newUrl.setMethod(HTTPConstants.GET);

                if (log.isDebugEnabled()) {
                    //log.debug("Potential <a href>match: " + newUrl);
                }
                if (true || HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
                    //log.debug("Matched!");
                    addURL(newUrl);
                } else {
                    //log.debug("Not matched: " + newUrl);
                }
            } catch (MalformedURLException e) {
                log.warn("Bad URL " + e);
            }
        }
    }

    private void addFramesetUrls(Document html, HTTPSampleResult result, HTTPSamplerBase config) {
        String base = "";
        NodeList baseList = html.getElementsByTagName("base"); // $NON-NLS-1$
        if (baseList.getLength() > 0) {
            base = baseList.item(0).getAttributes().getNamedItem("href") // $NON-NLS-1$
                    .getNodeValue();
        }
        NodeList nodeList = html.getElementsByTagName("frame"); // $NON-NLS-1$
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node tempNode = nodeList.item(i);
            NamedNodeMap nnm = tempNode.getAttributes();
            Node namedItem = nnm.getNamedItem("src"); // $NON-NLS-1$
            if (namedItem == null) {
                continue;
            }
            String hrefStr = namedItem.getNodeValue();
            try {
                HTTPSamplerBase newUrl = HtmlParsingUtils.createUrlFromAnchor(
                        hrefStr, ConversionUtils.makeRelativeURL(result.getURL(), base));
                newUrl.setMethod(HTTPConstants.GET);
                /*
                 * if (log.isDebugEnabled()) { log.debug("Potential <frame src>
                 * match: " + newUrl); }
                 *
                 */
                if (HtmlParsingUtils.isAnchorMatched(newUrl, config)) {
                    //log.debug("Matched!");
                    addURL(newUrl);
                }
            } catch (MalformedURLException e) {
                log.warn("Bad URL " + e);
            }
        }
    }

    private boolean hasNotVisited() {
        return !marks.isEmpty();
    }

    private HTTPSamplerBase getNextLink() {
        String key = marks.get(marks.size() - 1);
        if (log.isDebugEnabled()) {
            log.debug("Not visited key: " + key);
        }
        marks.remove(key);
        return URLs.get(key);
    }

    private void addURL(HTTPSamplerBase newUrl) {
        if (baseDomain == null) {
            baseDomain = newUrl.getDomain();
            if (log.isDebugEnabled()) {
                log.debug("Base domain: " + baseDomain);
            }
        }

        if (!newUrl.getDomain().equals(baseDomain)) {
            if (log.isDebugEnabled()) {
                //log.debug("Skip external: " + newUrl.toString());
            }
            return;
        }

        String key = getKeyForURL(newUrl);
        if (!URLs.containsKey(key)) {
            URLs.put(key, newUrl);
            marks.add(key);
            if (log.isDebugEnabled()) {
                log.debug("Put: " + newUrl.toString());
            }
        } else {
            if (log.isDebugEnabled()) {
                // log.debug("Skip existing: " + newUrl.toString());
            }
        }
    }

    private String getKeyForURL(HTTPSamplerBase newUrl) {
        PropertyIterator it = newUrl.propertyIterator();
        StringBuilder ret = new StringBuilder();
        while (it.hasNext()) {
            JMeterProperty p = it.next();
            ret.append(p.getName()).append("=").append(p.getStringValue()).append("\t");
        }
        return ret.toString();
    }
}
