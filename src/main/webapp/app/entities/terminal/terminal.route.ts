import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { TerminalComponent } from './terminal.component';
import { TerminalDetailComponent } from './terminal-detail.component';
import { TerminalPopupComponent } from './terminal-dialog.component';
import { TerminalDeletePopupComponent } from './terminal-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class TerminalResolvePagingParams implements Resolve<any> {

  constructor(private paginationUtil: PaginationUtil) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
      const page = route.queryParams['page'] ? route.queryParams['page'] : '1';
      const sort = route.queryParams['sort'] ? route.queryParams['sort'] : 'id,asc';
      return {
          page: this.paginationUtil.parsePage(page),
          predicate: this.paginationUtil.parsePredicate(sort),
          ascending: this.paginationUtil.parseAscending(sort)
    };
  }
}

export const terminalRoute: Routes = [
  {
    path: 'terminal',
    component: TerminalComponent,
    resolve: {
      'pagingParams': TerminalResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.terminal.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'terminal/:id',
    component: TerminalDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.terminal.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const terminalPopupRoute: Routes = [
  {
    path: 'terminal-new',
    component: TerminalPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.terminal.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'terminal/:id/edit',
    component: TerminalPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.terminal.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'terminal/:id/delete',
    component: TerminalDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.terminal.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
