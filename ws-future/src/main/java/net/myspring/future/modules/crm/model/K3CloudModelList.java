package net.myspring.future.modules.crm.model;

/**
 * Created by admin on 2016/12/21.
 */
public class K3CloudModelList {
    private K3CloudModel save;
    private K3CloudModel submit;
    private K3CloudModel audit;
    private K3CloudModel nextSubmit;
    private K3CloudModel nextAudit;

    public K3CloudModel getSave() {
        return save;
    }

    public void setSave(K3CloudModel save) {
        this.save = save;
    }

    public K3CloudModel getSubmit() {
        return submit;
    }

    public void setSubmit(K3CloudModel submit) {
        this.submit = submit;
    }

    public K3CloudModel getAudit() {
        return audit;
    }

    public void setAudit(K3CloudModel audit) {
        this.audit = audit;
    }

    public K3CloudModel getNextSubmit() {
        return nextSubmit;
    }

    public void setNextSubmit(K3CloudModel nextSubmit) {
        this.nextSubmit = nextSubmit;
    }

    public K3CloudModel getNextAudit() {
        return nextAudit;
    }

    public void setNextAudit(K3CloudModel nextAudit) {
        this.nextAudit = nextAudit;
    }
}
