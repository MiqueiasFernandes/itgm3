import { Injectable } from '@angular/core';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes, CanActivate } from '@angular/router';

import { UserRouteAccessService } from '../../shared';
import { PaginationUtil } from 'ng-jhipster';

import { ModeloExclusivoComponent } from './modelo-exclusivo.component';
import { ModeloExclusivoDetailComponent } from './modelo-exclusivo-detail.component';
import { ModeloExclusivoPopupComponent } from './modelo-exclusivo-dialog.component';
import { ModeloExclusivoDeletePopupComponent } from './modelo-exclusivo-delete-dialog.component';

import { Principal } from '../../shared';

@Injectable()
export class ModeloExclusivoResolvePagingParams implements Resolve<any> {

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

export const modeloExclusivoRoute: Routes = [
  {
    path: 'modelo-exclusivo',
    component: ModeloExclusivoComponent,
    resolve: {
      'pagingParams': ModeloExclusivoResolvePagingParams
    },
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.modeloExclusivo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }, {
    path: 'modelo-exclusivo/:id',
    component: ModeloExclusivoDetailComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.modeloExclusivo.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const modeloExclusivoPopupRoute: Routes = [
  {
    path: 'modelo-exclusivo-new',
    component: ModeloExclusivoPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.modeloExclusivo.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'modelo-exclusivo/:id/edit',
    component: ModeloExclusivoPopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.modeloExclusivo.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  },
  {
    path: 'modelo-exclusivo/:id/delete',
    component: ModeloExclusivoDeletePopupComponent,
    data: {
        authorities: ['ROLE_USER'],
        pageTitle: 'itgmApp.modeloExclusivo.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
