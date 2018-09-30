package ns.flex.file {
import mx.containers.VBox;
import mx.rpc.events.ResultEvent;
import mx.rpc.remoting.mxml.RemoteObject;

import ns.flex.util.ArrayUtil;

public class Downloader extends VBox {

    [Bindable]
    public var deletable:Boolean = false;

    private var _service:RemoteObject;

    public function set dataProdiver(list:*):void {
        removeAllChildren();
        for each (var fileInfo:Object in ArrayUtil.toArray(list)) {
            var fileItem:FileItem = new FileItem;
            fileItem.service = _service;
            fileItem.fileId = fileInfo.fileId;
            fileItem.ownerId = fileInfo.ownerId;
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
    public function set service(service:RemoteObject):void {
        _service = service;
        _service.getOperation('queryAttachByOwner').addEventListener(ResultEvent.RESULT,
                function (e:ResultEvent):void {
                    dataProdiver = e.result;
                });
    }
}
}
