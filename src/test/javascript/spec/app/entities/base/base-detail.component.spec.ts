import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { ItgmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { BaseDetailComponent } from '../../../../../../main/webapp/app/entities/base/base-detail.component';
import { BaseService } from '../../../../../../main/webapp/app/entities/base/base.service';
import { Base } from '../../../../../../main/webapp/app/entities/base/base.model';

describe('Component Tests', () => {

    describe('Base Management Detail Component', () => {
        let comp: BaseDetailComponent;
        let fixture: ComponentFixture<BaseDetailComponent>;
        let service: BaseService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ItgmTestModule],
                declarations: [BaseDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    BaseService,
                    EventManager
                ]
            }).overrideComponent(BaseDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(BaseDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(BaseService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Base(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.base).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
