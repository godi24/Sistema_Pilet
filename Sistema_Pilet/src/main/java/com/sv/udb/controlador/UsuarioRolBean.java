/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import static com.fasterxml.jackson.databind.util.ClassUtil.getRootCause;
import com.sv.udb.ejb.UsuarioRolFacadeLocal;
import com.sv.udb.modelo.UsuarioRol;
import com.sv.udb.utils.LOG4J;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.log4j.Logger;
import org.primefaces.context.RequestContext;

/**
 * Esta clase se encarga de manejar lo relacionado al CRUD de la tabla de asignación UsuarioRol
 * @author: AGAV Team
 * @version: Prototipo 1
 */
@Named(value = "usuarioRolBean")
@SessionScoped
public class UsuarioRolBean implements Serializable {
    //Bean de session
    @Inject
    private LoginBean logiBean; 
    //Campos de la clase
    @EJB
    private UsuarioRolFacadeLocal FCDEUsuaRole;
    
    private UsuarioRol objeUsuaRole;
    private UsuarioRol objeVali;
    private List<UsuarioRol> listUsuaRole;
    private List<UsuarioRol> listUsua;
    private boolean guardar;
    
    private LOG4J<UsuarioRolBean> lgs = new LOG4J<UsuarioRolBean>(UsuarioRolBean.class) {
    };
    private Logger log = lgs.getLog();

    //Encapsulamiento de los campos de la clase
    
    public UsuarioRol getObjeUsuaRole() {
        return objeUsuaRole;
    }

    public void setObjeUsuaRole(UsuarioRol objeUsuaRole) {
        this.objeUsuaRole = objeUsuaRole;
    }

    public void setObjeVali(UsuarioRol objeVali) {
        this.objeVali = objeVali;
    }
    
    public List<UsuarioRol> getListUsuaRole() {
        return listUsuaRole;
    }

    public boolean isGuardar() {
        return guardar;
    }

    public List<UsuarioRol> getListUsua() {
        return listUsua;
    }
    
    /**
     * Constructor de la clase UsuarioRolBean
     */
    public UsuarioRolBean() {
    }
    
    /**
     * Método que se ejecuta después de la construcción de la clase e inicializa las variables
     */
    @PostConstruct
    public void init()
    {
        this.limpForm();
        this.consTodo();
        //log = new LOG4J();
        //log.debug("Se inicializa el modelo de UsuarioRol");
    }
    
    /**
     * Método que limpia el formulario reiniciando las variables
     */
    public void limpForm()
    {
        this.objeUsuaRole = new UsuarioRol();
        this.guardar = true;        
    }
    
    /**
    * Método que valida si ya existe un registro ingresado
    * @return el valor de true = si no hay registros duplicados y false = si hay registros duplicados para posteriormente mostrar un error
    */
    public boolean valiUsuaRole()
    {
        boolean resp = true;
        this.objeVali = FCDEUsuaRole.findByUsuaAndRole(this.objeUsuaRole.getCodiUsua(), this.objeUsuaRole.getCodiRole());
        if(this.objeVali != null)
            resp = (this.objeVali.getCodiUsuaRole() == this.objeUsuaRole.getCodiUsuaRole()) ? true : false;
        return resp;
    }
    
    /**
     * Método que guarda la información en la base de datos
     */
    public void guar()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(valiUsuaRole()){
                FCDEUsuaRole.create(this.objeUsuaRole);
                this.listUsuaRole.add(this.objeUsuaRole);
                this.guardar = false;
                log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Usuariorol creado: "+this.objeUsuaRole.getCodiUsua()+"/"+this.objeUsuaRole.getCodiRole());
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos guardados')");
            }
            else
               ctx.execute("setMessage('MESS_WARN', 'Atención', 'Datos ya registrados')"); 
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al guardar ')");
            ex.printStackTrace();
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Error creando Usuariorol: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    /**
     * Método que modifica la información en la base de datos
     */
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        try
        {
            if(valiUsuaRole()){
                this.listUsuaRole.remove(this.objeUsuaRole); //Limpia el objeto viejo
                FCDEUsuaRole.edit(this.objeUsuaRole);
                this.listUsuaRole.add(this.objeUsuaRole); //Agrega el objeto modificado
                log.info(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Usuariorol modificado: "+this.objeUsuaRole.getCodiUsuaRole());
                ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Datos Modificados')");
            }
            else
               ctx.execute("setMessage('MESS_WARN', 'Atención', 'Datos ya registrados')"); 
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al modificar ')");
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Error modificando Usuariorol: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
    
    /**
     * Método que consulta la información de la base de datos
     */
    public void consTodo()
    {
        try
        {
            this.listUsuaRole = FCDEUsuaRole.findAll();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            
        }
    }
    
    /**
     * Método que consutla la información de un registro en específico de la base de datos
     */
    public void cons()
    {
        RequestContext ctx = RequestContext.getCurrentInstance(); //Capturo el contexto de la página
        int codi = Integer.parseInt(FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("codiObjePara"));
        try
        {
            this.objeUsuaRole = FCDEUsuaRole.find(codi);
            this.guardar = false;
            ctx.execute("setMessage('MESS_SUCC', 'Atención', 'Consultado a " + 
                    String.format("%s", this.objeUsuaRole.getCodiUsua().getAcceUsua()) + "')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO', 'Atención', 'Error al consultar')");
            log.error(logiBean.getObjeUsua().getCodiUsua()+"-"+"UsuarioRol"+"-"+"Error consultando Usuariorol: "+getRootCause(ex).getMessage());
        }
        finally
        {
            
        }
    }
}