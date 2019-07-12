package neo.script.gorm.general.controller

import neo.script.gorm.general.service.AttachmentService
import net.unicon.cas.client.configuration.CasClientConfigurationProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.CacheControl
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.view.RedirectView

import javax.activation.FileTypeMap
import javax.servlet.http.HttpSession
import java.util.concurrent.TimeUnit

@RestController
class AttachmentController {
    @Autowired
    AttachmentService attachmentService
    @Autowired(required = false)
    CasClientConfigurationProperties casProps;

    @GetMapping("attach/{id}")
    public ResponseEntity<byte[]> getAttach(@PathVariable("id") String id) throws IOException {
        def info = attachmentService.get(id)
        if (info) {
            def file = attachmentService.getFile(info.fileId)
            return ResponseEntity.ok()
                    .header("Content-Disposition", "attachment; filename=" + info.getName())
                    .cacheControl(CacheControl.maxAge(10, TimeUnit.DAYS).cachePublic().noTransform())
                    .contentType(MediaType.valueOf(FileTypeMap.getDefaultFileTypeMap().getContentType(info.name)))
                    .body(file.data);
        } else
            return ResponseEntity.notFound().build()
    }

    @GetMapping("casLogout")
    public RedirectView casLogout(HttpSession session) {
        session.invalidate()
        return new RedirectView("$casProps.serverUrlPrefix/logout?service=$casProps.clientHostUrl/index.html");
    }
}
