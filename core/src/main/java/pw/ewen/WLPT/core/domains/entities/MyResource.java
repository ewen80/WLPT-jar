package pw.ewen.WLPT.core.domains.entities;

import pw.ewen.WLPT.core.domains.Resource;

import javax.persistence.Entity;
import java.io.Serializable;

/*
 * 代表权限模块管理的资源
 *  
 */
@Entity
public  class MyResource extends Resource implements Serializable {

//    private ResourceType resourceType;

    private int number;

    public MyResource(int number) {
        this.number = number;
    }
    public MyResource() {

    }

//    @ManyToOne
//    @JoinColumn(name = "resourceType_Id")
//    public ResourceType getResourceType() {
//        return resourceType;
//    }
//    public void setResourceType(ResourceType resourceType) {
//        this.resourceType = resourceType;
//    }

    //测试用
    public int getNumber() {
        return number;
    }
    public void setNumber(int number) {
        this.number = number;
    }
}
