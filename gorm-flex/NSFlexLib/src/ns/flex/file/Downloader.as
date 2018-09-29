package ns.flex.file {
import mx.containers.VBox;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import ns.flex.common.Constants;

import ns.flex.util.ArrayUtil;
import ns.flex.util.RemoteUtil;

public class Downloader extends VBox {

    [Bindable]
    public var deletable:Boolean = false;

    private var service:RemoteObject;
    private var _ownerId:String;
    private var _destination:String;
    private var ownerChanged:Boolean = false;

    public function Downloader(destination:String = null) {
        super();
        if (destination)
            this.destination = destination;
    }

    public function set dataProdiver(list:*):void {
        removeAllChildren();
        for each (var fileInfo:Object in ArrayUtil.toArray(list)) {
            var fileItem:FileItem = new FileItem;
            fileItem.destination = _destination;
            fileItem.fileId = fileInfo.fileId;
            fileItem.ownerId = _ownerId;
            fileItem.fileName = fileInfo.name;
            fileItem.fileSize = fileInfo.fileSize;
            fileItem.dateCreated = fileInfo.dateCreated;
            fileItem.deletable = deletable;
            addChild(fileItem);
        }
    }

    /**
     * 赋值后查询，如果不用查询，直接设置service
     * @param destination
     */
    public function set destination(destination:String):void {
        _destination = destination;
        service = RemoteUtil.createRemoteObject(destination, null, null, true);
        service.getOperation('queryAttachByOwner').addEventListener(ResultEvent.RESULT,
                function (e:ResultEvent):void {
                    dataProdiver = e.result;
                });
        queryAttachByOwner();
    }

    public function set ownerId(ownerId:String):void {
        _ownerId = ownerId;
        ownerChanged = true;
        queryAttachByOwner();
    }

    private function queryAttachByOwner():void {
        if (_ownerId == null)
            dataProdiver = null;
        else if (ownerChanged && service) {
            service.queryAttachByOwner(_ownerId);
            ownerChanged = false;
        }
    }
}
}
