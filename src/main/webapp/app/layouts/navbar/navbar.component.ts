import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiLanguageService } from 'ng-jhipster';

import { ProfileService } from '../profiles/profile.service'; // FIXME barrel doesn't work here
import { JhiLanguageHelper, Principal, LoginModalService, LoginService } from '../../shared';

import {EventManager} from 'ng-jhipster';
import { VERSION, DEBUG_INFO_ENABLED } from '../../app.constants';
import {Compartilhar} from '../../entities/compartilhar/compartilhar.model';
import {ShareService} from '../share/share.service';
import {SidebarService} from '../sidebar/sidebar.service';
import {CustomizeService} from "../../entities/customize/customize.service";
import {AccountService} from "../../shared/auth/account.service";

@Component({
    selector: 'jhi-navbar',
    templateUrl: './navbar.component.html',
    styleUrls: [
        'navbar.scss'
    ]
})
export class NavbarComponent implements OnInit {

    inProduction: boolean;
    isNavbarCollapsed: boolean;
    languages: any[];
    swaggerEnabled: boolean;
    modalRef: NgbModalRef;
    version: string;
    compartilhamentos: Compartilhar[];
    bellN  = 0;
    showEntites = false;
    serverOnLine = false;

    constructor(
        private loginService: LoginService,
        private languageHelper: JhiLanguageHelper,
        private languageService: JhiLanguageService,
        private principal: Principal,
        private eventManager: EventManager,
        private loginModalService: LoginModalService,
        private profileService: ProfileService,
        private router: Router,
        private sidebarService: SidebarService,
        private shareService: ShareService,
        private customizeService: CustomizeService,
        private account: AccountService,

    ) {
        this.version = DEBUG_INFO_ENABLED ? 'v' + VERSION : '';
        this.isNavbarCollapsed = true;
        this.languageService.addLocation('home');
    }

    ngOnInit() {
        this.languageHelper.getAll().then((languages) => {
            this.languages = languages;
        });

        this.profileService.getProfileInfo().subscribe((profileInfo) => {
            this.inProduction = profileInfo.inProduction;
            this.swaggerEnabled = profileInfo.swaggerEnabled;
        });

        this.shareService.shareObserver$.subscribe((compartilhamentos: Compartilhar[]) =>{
            this.compartilhamentos = compartilhamentos;
            this.bellN = compartilhamentos.length;
        });

        this.shareService.consultarCompartilhamentos();

        this.sidebarService.openSidebar();

        this.customizeService.getDesktop().subscribe((desktop: any) => {
                this.showEntites = (desktop.entidades === true);
        });

        this.principal.identity().then((id) => {
            if(id && !this.showEntites) {
                if (id.authorities && (id.authorities.indexOf("ROLE_ADMIN") !== -1)){
                    this.customizeService.setMenuEntidades(true);
                }
            }
            this.sidebarService.openSidebar();
        });

        this.eventManager.subscribe('customizeListModification', () => {
            this.customizeService.getDesktop().subscribe((desktop: any) => {
                if (desktop) {
                    this.showEntites = (desktop.entidades === true);
                }
            });
        });

        this.account.observeServerStatus$.subscribe((status) => {
            this.serverOnLine = status;
        });

    }

    changeLanguage(languageKey: string) {
      this.languageService.changeLanguage(languageKey);
    }

    collapseNavbar() {
        this.isNavbarCollapsed = true;
        this.toogleSidebar();
    }

    isAuthenticated() {
        return this.principal.isAuthenticated();
    }

    login() {
        this.modalRef = this.loginModalService.open();
    }

    logout() {
        this.collapseNavbar();
        this.loginService.logout();
        this.router.navigate(['']);
    }

    toggleNavbar() {
        this.isNavbarCollapsed = !this.isNavbarCollapsed;
    }

    getImageUrl() {
        return this.isAuthenticated() ? this.principal.getImageUrl() : null;
    }

    toogleSidebar() {
        this.sidebarService.toogleSidebar();
    }

    receberCompartilhamento(compartilhar: Compartilhar){
        this.collapseNavbar();
        this.shareService.receber(compartilhar);
    }

    consultar() {
        this.shareService.consultarCompartilhamentos();
    }

    getServerStatus() {
        this.account.getEndereco().subscribe(() => {});
    }
}
