import { Injectable } from '@angular/core';
import {Subject} from 'rxjs/Subject';

import {Principal} from '../../shared';
import {Customize, CustomizeService} from "../../entities/customize";
import {EventManager} from 'ng-jhipster';
@Injectable()
export class SidebarService {

    private isSidebarOpen = true;
    private isLockedSidebar = true;

    private observeSidebarStatus = new Subject<boolean>();
    private observeLockedStatus = new Subject<boolean>();

    public sidebarObserver$ = this.observeSidebarStatus.asObservable();
    public lockedObserver$ = this.observeLockedStatus.asObservable();

    constructor(
        private principal: Principal,
        private customizeService: CustomizeService,
        private eventManager: EventManager,
    ) {
        this.eventManager.subscribe('customizeListModification', () => {
            this.customizeService.getCustomize()
                .subscribe(
                    (customize: Customize) => {
                        if (customize) {
                            this.isLockedSidebar = customize.sidebar ? customize.sidebar : false;
                            this.updateLockedSidebar();
                        }
                    }
                );
        });

        this.eventManager.subscribe('authenticationSuccess', (message) => {
            this.openSidebar();
        });

        this.eventManager.subscribe('logout', () =>{
            this.isSidebarOpen = false;
            this.updateSidebarOpen();
        });
    }

    public openSidebar() {
        this.isSidebarOpen = this.principal.isAuthenticated();
        this.updateSidebarOpen();
    }

    public closeSidebar() {
        this.isSidebarOpen =
            this.principal.isAuthenticated() &&
            this.isSidebarOpen &&
            this.isLockedSidebar;
        this.updateSidebarOpen();
    }

    public toogleSidebar() {
        if (this.isSidebarOpen) {
            this.closeSidebar();
        } else {
            this.openSidebar();
        }
    }

    public toogleSidebarFixed() {
        this.isLockedSidebar = !this.isLockedSidebar;
        this.updateLockedSidebar();
        this.customizeSidebar();
    }

    public lockSidebar() {
        this.isLockedSidebar = true;
        this.updateLockedSidebar();
        this.customizeSidebar();
    }

    public unLockSidebar() {
        this.isLockedSidebar = false;
        this.updateLockedSidebar();
        this.customizeSidebar();
    }

    private updateSidebarOpen() {
        this.observeSidebarStatus.next(this.isSidebarOpen);
    }

    private updateLockedSidebar() {
        this.observeLockedStatus.next(this.isLockedSidebar);
    }

    private customizeSidebar(){
        this.customizeService.customizeSidebar(this.isLockedSidebar);
    }

    public isOpen():boolean {
        return this.isSidebarOpen;
    }

    public isLock():boolean {
        return this.isLockedSidebar;
    }
}
