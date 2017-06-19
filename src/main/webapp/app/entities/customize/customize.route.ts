import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CustomizeComponent } from './customize.component';
import { CustomizeDetailComponent } from './customize-detail.component';
import { CustomizePopupComponent } from './customize-dialog.component';
import { CustomizeDeletePopupComponent } from './customize-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CustomizeResolvePagingParams implements Resolve<any> {

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

export const customizeRoute: Routes = [
  {
    path: 'customize',
    component: CustomizeComponent,
    resolve: {
      'pagingParams': CustomizeResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.customize.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'customize/:id',
    component: CustomizeDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.customize.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const customizePopupRoute: Routes = [
  {
    path: 'customize-new',
    component: CustomizePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.customize.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'customize/:id/edit',
    component: CustomizePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.customize.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'customize/:id/delete',
    component: CustomizeDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.customize.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
