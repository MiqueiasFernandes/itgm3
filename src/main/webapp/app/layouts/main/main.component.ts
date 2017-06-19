import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRouteSnapshot, NavigationEnd, RoutesRecognized } from '@angular/router';

import { JhiLanguageHelper, StateStorageService } from '../../shared';
import {SidebarService} from '../sidebar/sidebar.service';

@Component({
    selector: 'jhi-main',
    templateUrl: './main.component.html'
})
export class JhiMainComponent implements OnInit {

    isSideBarOpen: Boolean = false;

    constructor(
        private jhiLanguageHelper: JhiLanguageHelper,
        private router: Router,
        private $storageService: StateStorageService,
        private sidebarService: SidebarService
    ) {
        sidebarService.sidebarObserver$.subscribe((open: boolean) => {
            this.isSideBarOpen = open;
        });
    }

    openSidebar() {
        this.sidebarService.openSidebar();
    }

    closeSidebar() {
        this.sidebarService.closeSidebar();
    }

    private getPageTitle(routeSnapshot: ActivatedRouteSnapshot) {
        let title: string = (routeSnapshot.data && routeSnapshot.data['pageTitle']) ? routeSnapshot.data['pageTitle'] : 'itgmApp';
        if (routeSnapshot.firstChild) {
            title = this.getPageTitle(routeSnapshot.firstChild) || title;
        }
        return title;
    }

    ngOnInit() {
        this.router.events.subscribe((event) => {
            if (event instanceof NavigationEnd) {
                this.jhiLanguageHelper.updateTitle(this.getPageTitle(this.router.routerState.snapshot.root));
            }
        });
    }


    noIsHome(): boolean {
        return !this.router.isActive('/', true);
    }
}
