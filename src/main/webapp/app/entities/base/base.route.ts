import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { BaseComponent } from './base.component';
import { BaseDetailComponent } from './base-detail.component';
import { BasePopupComponent } from './base-dialog.component';
import { BaseDeletePopupComponent } from './base-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class BaseResolvePagingParams implements Resolve<any> {

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

export const baseRoute: Routes = [
  {
    path: 'base',
    component: BaseComponent,
    resolve: {
      'pagingParams': BaseResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.base.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'base/:id',
    component: BaseDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.base.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const basePopupRoute: Routes = [
  {
    path: 'base-new',
    component: BasePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.base.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'base/:id/edit',
    component: BasePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.base.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'base/:id/delete',
    component: BaseDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.base.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
