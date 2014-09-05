package Coalesce.Framework.DataModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.joda.time.DateTime;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import Coalesce.Common.Helpers.StringHelper;
import Coalesce.Common.Helpers.XmlHelper;

/*-----------------------------------------------------------------------------'
 Copyright 2014 - InCadence Strategic Solutions Inc., All Rights Reserved

 Notwithstanding any contractor copyright notice, the Government has Unlimited
 Rights in this work as defined by DFARS 252.227-7013 and 252.227-7014.  Use
 of this work other than as specifically authorized by these DFARS Clauses may
 violate Government rights in this work.

 DFARS Clause reference: 252.227-7013 (a)(16) and 252.227-7014 (a)(16)
 Unlimited Rights. The Government has the right to use, modify, reproduce,
 perform, display, release or disclose this computer software and to have or
 authorize others to do so.

 Distribution Statement D. Distribution authorized to the Department of
 Defense and U.S. DoD contractors only in support of U.S. DoD efforts.
 -----------------------------------------------------------------------------*/

public class CoalesceEntitySyncShell {

    // -----------------------------------------------------------------------//
    // protected Member Variables
    // -----------------------------------------------------------------------//

    private Document _DataObjectDocument;
    private Node _EntityNode;

    // -----------------------------------------------------------------------//
    // Static Creates
    // -----------------------------------------------------------------------//

    public static CoalesceEntitySyncShell Create(XsdEntity Entity) throws SAXException, IOException
    {
        return CoalesceEntitySyncShell.Create(Entity.ToXml());
    }

    public static CoalesceEntitySyncShell Create(String EntitySyncShellXml) throws SAXException, IOException
    {
        return CoalesceEntitySyncShell.Create(XmlHelper.loadXMLFrom(EntitySyncShellXml));
    }

    public static CoalesceEntitySyncShell Create(Document doc) throws SAXException, IOException
    {
        // Create a new CoalesceEntityTemplate
        CoalesceEntitySyncShell EntitySyncShell = new CoalesceEntitySyncShell();

        // Initialize
        if (!EntitySyncShell.Initialize(doc)) return null;

        // return
        return EntitySyncShell;
    }

    // -----------------------------------------------------------------------//
    // Initialization
    // -----------------------------------------------------------------------//

    public boolean Initialize(XsdEntity Entity) throws SAXException, IOException
    {
        return this.Initialize(Entity.ToXml());
    }

    public boolean Initialize(String entityXml) throws SAXException, IOException
    {
        return this.Initialize(XmlHelper.loadXMLFrom(entityXml));
    }

    public boolean Initialize(Document doc)
    {
        // Prune Nodes
        CoalesceEntitySyncShell.PruneNodes(doc);

        // Set DataObjectDocument
        this.SetDataObjectDocument(doc);

        // return Success
        return true;
    }

    // -----------------------------------------------------------------------//
    // public Properties
    // -----------------------------------------------------------------------//

    public Document GetDataObjectDocument()
    {
        return this._DataObjectDocument;
    }

    public void SetDataObjectDocument(Document value)
    {
        this._DataObjectDocument = value;
        this._EntityNode = value.getElementsByTagName("entity").item(0);
    }

    public Node GetEntityNode()
    {
        return this._EntityNode;
    }

    public void SetEntityNode(Node value)
    {
        this._EntityNode = value;
    }

    public String toXml()
    {
        return XmlHelper.FormatXml(this._DataObjectDocument);
    }

    // -----------------------------------------------------------------------//
    // public Static Functions
    // -----------------------------------------------------------------------//

    public static CoalesceEntitySyncShell Clone(CoalesceEntitySyncShell SyncShell)
    {

        try
        {
            // Create new Instance
            CoalesceEntitySyncShell SyncShellClone = new CoalesceEntitySyncShell();

            // Initialize
            // TODO: make sure .Clone's are same between vb and java. Java required a boolean.
            // return SyncShellClone.Initialize(SyncShell.DataObjectDocument.Clone) //vb
            // return SyncShellClone.Initialize(SyncShell.GetDataObjectDocument()); //1st java thought
            SyncShellClone.Initialize((Document) SyncShell.GetDataObjectDocument().cloneNode(true));
            return SyncShellClone;
            // return CallResult.failedCallResult; //SyncShellClone.InitializeFromEntity((CoalesceEntity)
            // SyncShell.GetDataObjectDocument().cloneNode(true));

        }
        catch (Exception ex)
        {
            // return Failed Error
            return null;
        }
    }

    public static CoalesceEntitySyncShell GetRequiredChangesSyncShell(CoalesceEntitySyncShell LocalFullSyncShell,
                                                                      CoalesceEntitySyncShell RemoteFullSyncShell)
    {
        try
        {
            // Create the RequiredChangesSyncShell as a Clone of the RemoteFullSyncShell. We will
            // then prune out nodes that aren't required recursively as we compare against
            // the nodes in LocalFullSyncShell.
            CoalesceEntitySyncShell requiredChangesSyncShell = CoalesceEntitySyncShell.Clone(RemoteFullSyncShell);

            if (requiredChangesSyncShell.equals(null)) return requiredChangesSyncShell;

            // Prune Unchanged Nodes
            CoalesceEntitySyncShell.PruneUnchangedNodes(CoalesceEntitySyncShell.GenerateMap(LocalFullSyncShell.GetDataObjectDocument()),
                                                        RemoteFullSyncShell.GetDataObjectDocument(),
                                                        CoalesceEntitySyncShell.GenerateMap(requiredChangesSyncShell.GetDataObjectDocument()));

            return requiredChangesSyncShell;

        }
        catch (Exception ex)
        {
            // return Failed Error
            return null;
        }
    }

    // ----------------------------------------------------------------------//
    // Private Static Functions
    // ----------------------------------------------------------------------//

    private static boolean PruneNodes(Node NodeToPrune)
    {
        try
        {
            boolean isSuccess = false;

            // TODO: Make sure the Node/Attribute "switch" is ok
            // Prune Unnecessary Attributes
            if (NodeToPrune.getAttributes() != null)
            {
                if (NodeToPrune.getAttributes().getLength() > 0)
                {
                    ArrayList<String> RemoveList = new ArrayList<String>();
                    NamedNodeMap attributeList = NodeToPrune.getAttributes();

                    // Find Attributes to Remove
                    for (int i = 0; i < attributeList.getLength(); i++)
                    {
                        // Get Attribute Name
                        String attributeName = attributeList.item(i).getNodeName();

                        switch (attributeName.toUpperCase()) {
                        case "KEY":
                        case "LASTMODIFIED":
                        case "HASH":
                            // Keep
                            break;
                        default:
                            // Mark for Deletion
                            RemoveList.add(attributeName);
                        }
                    }

                    // Remove Attributes
                    for (String attributeName : RemoveList)
                    {
                        attributeList.removeNamedItem(attributeName);
                    }
                }
            }

            // Prune Unnecessary Nodes
            if (NodeToPrune.hasChildNodes())
            {
                if (NodeToPrune.getChildNodes().getLength() > 0)
                {
                    ArrayList<Node> RemoveList = new ArrayList<Node>();
                    NodeList children = NodeToPrune.getChildNodes();

                    // Find Nodes to Remove
                    // for (Node ChildNode : NodeToPrune.getChildNodes()){
                    for (int i = 0; i < children.getLength(); i++)
                    {
                        Node ChildNode = children.item(i);

                        switch (ChildNode.getNodeType()) {
                        case Node.ELEMENT_NODE:
                            // Keep
                            break;
                        default:
                            // Add to Remove List
                            RemoveList.add(ChildNode);
                        }
                    }

                    // Remove
                    for (Node ChildNode : RemoveList)
                    {
                        NodeToPrune.removeChild(ChildNode);
                    }
                }
            }

            // Recurse Child Nodes
            for (int i = 0; i < NodeToPrune.getChildNodes().getLength(); i++)
            {
                Node ChildNode = NodeToPrune.getChildNodes().item(i);
                isSuccess = PruneNodes(ChildNode);
            }

            // return Success
            return isSuccess;

        }
        catch (Exception ex)
        {
            // return Failed Error
            return false;
        }
    }

    private static void PruneUnchangedNodes(HashMap<String, Node> localNodes,
                                            Node RemoteSyncShellNode,
                                            HashMap<String, Node> requiredNodes)
    {
        /*
         * Recurse Child Nodes (Important: Because this us up front, we check leaf nodes first, which is necessary for
         * correct pruning. We rely on whether or not a node has children remaining as one of the decision points on whether
         * or not the node itself needs to remain.)
         */
        NodeList children = RemoteSyncShellNode.getChildNodes();

        for (int ii = 0; ii < children.getLength(); ii++)
        {
            // Recursive
            CoalesceEntitySyncShell.PruneUnchangedNodes(localNodes, children.item(ii), requiredNodes);
        }

        // Check RemoteSyncShellNode
        String key = XmlHelper.GetAttribute(RemoteSyncShellNode, "key");

        if (key != "")
        {
            // Evaluate Based on the Coalesce Object Type
            switch (RemoteSyncShellNode.getNodeName().toUpperCase()) {
            case "FIELD":
            case "LINKAGE":
            case "FIELDHISTORY":
            case "FIELDDEFINITION":
                if (!CoalesceEntitySyncShell.IsNewer(localNodes.get(key), RemoteSyncShellNode))
                {
                    /*
                     * Local is newer or the same date; Prune from the RequiredChangesSyncShell IF there are no remaining
                     * children below the node. If there are children, then we keep the node even if it's older.
                     */
                    CoalesceEntitySyncShell.PruneNode(requiredNodes.get(key));
                }
                break;
            case "LINKAGESECTION":
            case "RECORD":
            case "RECORDSET":
            case "SECTION":
            case "ENTITY":
            default:
                /*
                 * For these Coalesce Objects, we will check the RequiredChangesSyncShell for the presence of this object's
                 * node. If the node is present, and it still has children, then we will keep the node in the
                 * RequiredChangesSyncShell. Since we prune leaves first, and work our way up the tree to the base Entity
                 * node, the presence of child nodes means that a child object to this object required updating, therefore we
                 * have to keep this object's node. If there are no child nodes, then we can prune this object's node.
                 */
                CoalesceEntitySyncShell.PruneNode(requiredNodes.get(key));
                break;
            }
        }

    }

    private static void PruneNode(Node node)
    {
        if (node != null && node.getParentNode() != null && !node.hasChildNodes())
        {
            node.getParentNode().removeChild(node);
        }
    }

    private static boolean IsNewer(Node oldNode, Node newNode)
    {
        boolean isNewer = true;

        if (oldNode != null)
        {

            DateTime oldModified = XmlHelper.GetAttributeAsDate(oldNode, "lastmodified");
            DateTime newLastModified = XmlHelper.GetAttributeAsDate(newNode, "lastmodified");

            switch (oldModified.compareTo(newLastModified)) {
            case 0:
            case 1:
                isNewer = false;
                break;
            default:
                isNewer = true;
                break;
            }

        }
        else
        {
            // Remote Node not found in LocalFullSyncShell; Keep in the RequiredChangesSyncShell
        }

        return isNewer;
    }

    private static HashMap<String, Node> GenerateMap(Document doc)
    {

        HashMap<String, Node> nodeMap = new HashMap<String, Node>();

        NodeList nodeList = doc.getElementsByTagName("*");

        for (int jj = 0; jj < nodeList.getLength(); jj++)
        {
            Node node = nodeList.item(jj);

            if (node.getNodeType() == Node.ELEMENT_NODE)
            {
                String nodeKey = XmlHelper.GetAttribute(node, "key");

                if (!StringHelper.IsNullOrEmpty(nodeKey))
                {
                    nodeMap.put(nodeKey, node);
                }
            }
        }

        return nodeMap;

    }

}
