import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { CompartilharComponent } from './compartilhar.component';
import { CompartilharDetailComponent } from './compartilhar-detail.component';
import { CompartilharPopupComponent } from './compartilhar-dialog.component';
import { CompartilharDeletePopupComponent } from './compartilhar-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class CompartilharResolvePagingParams implements Resolve<any> {

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

export const compartilharRoute: Routes = [
  {
    path: 'compartilhar',
    component: CompartilharComponent,
    resolve: {
      'pagingParams': CompartilharResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.compartilhar.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'compartilhar/:id',
    component: CompartilharDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.compartilhar.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const compartilharPopupRoute: Routes = [
  {
    path: 'compartilhar-new',
    component: CompartilharPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.compartilhar.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'compartilhar/:id/edit',
    component: CompartilharPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.compartilhar.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'compartilhar/:id/delete',
    component: CompartilharDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.compartilhar.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
